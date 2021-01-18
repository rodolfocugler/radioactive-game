package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.*;
import br.com.rodolfocugler.dtos.*;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

  public ReportService(AccountGroupService accountGroupService,
                       TransportService transportService,
                       MessageService messageService,
                       EnvironmentService environmentService) {
    this.accountGroupService = accountGroupService;
    this.transportService = transportService;
    this.messageService = messageService;
    this.environmentService = environmentService;
  }

  private final AccountGroupService accountGroupService;
  private final TransportService transportService;
  private final MessageService messageService;
  private final EnvironmentService environmentService;

  public ReportDTO getByAccountGroupId(long accountGroupId) throws DataNotFoundException {
    List<Environment> environments = environmentService.get();
    AccountGroup accountGroup = accountGroupService.get(accountGroupId);
    Map<String, String> accounts = accountGroup.getAccounts().stream()
            .collect(Collectors.toMap(Account::getNumber, Account::getName));

    List<Transport> transports = transportService.getByAccountGroupId(accountGroupId);
    transports = transports.subList(2, transports.size());
    List<TransportEventDTO> transportEvents = transports.stream().map(t -> TransportEventDTO
            .builder().timestamp(t.getTimestamp()).accounts(new ArrayList<>(accounts.values()))
            .from(t.getFromEnvironment().getName()).to(t.getToEnvironment().getName())
            .tools(t.getTools().stream().map(Tool::getDescription).collect(Collectors.toList()))
            .build()).collect(Collectors.toList());

    Map<String, List<ChatDTO>> messages = environments.stream().collect(Collectors
            .toMap(Environment::getName, e -> {
              List<ChatMessage> msgs = messageService
                      .getByEnvironmentId(e.getId(), accountGroupId);
              return parseToChartDTO(msgs);
            }));

    messages.put("Geral", parseToChartDTO(messageService
            .getByEnvironmentId(null, accountGroupId)));

    Map<String, List<QuestionDTO>> questions = environments.stream().collect(Collectors
            .toMap(Environment::getName, e -> {
                      if (e.getQuestions() == null) return new ArrayList<>();
                      return e.getQuestions().stream()
                              .map(q -> mapToQuestion(q, accountGroupId)).collect(Collectors.toList());
                    }
            ));


    ReportDTO report = ReportDTO.builder()
            .accounts(accounts)
            .messages(messages)
            .events(transportEvents)
            .questions(questions)
            .build();

    fillUpTimes(messages, transportEvents, report);
    return report;
  }

  private QuestionDTO mapToQuestion(Question q, long accountGroupId) {
    List<ResponseDTO> responses = q.getResponses().stream()
            //.filter(r -> r.getAccount().getAccountGroup().getId() == accountGroupId)
            .map(r -> ResponseDTO.builder().account(r.getAccount().getName())
                    .timestamp(r.getTimestamp()).response(r.getText()).build())
            .collect(Collectors.toList());

    return QuestionDTO.builder().question(q.getDescription())
            .responses(responses).build();
  }

  private void fillUpTimes(Map<String, List<ChatDTO>> messages,
                           List<TransportEventDTO> transportEvents, ReportDTO report) {
    long[] messageTs = messages.values().stream().flatMap(List::stream).mapToLong(ChatDTO::getTimestamp)
            .toArray();
    long[] transportTs = transportEvents.stream().mapToLong(TransportEventDTO::getTimestamp)
            .toArray();

    long minTs = Math.min(Arrays.stream(messageTs).min().orElse(Long.MAX_VALUE),
            Arrays.stream(transportTs).min().orElse(Long.MAX_VALUE));
    long maxTs = Math.max(Arrays.stream(messageTs).max().orElse(Long.MIN_VALUE),
            Arrays.stream(transportTs).max().orElse(Long.MIN_VALUE));

    report.setStartTime(minTs);
    report.setEndTime(maxTs);
    report.setDuration(maxTs - minTs);
  }

  private List<ChatDTO> parseToChartDTO(List<ChatMessage> messages) {
    return messages.stream().map(m -> ChatDTO.builder().text(m.getText())
            .accounts(m.getAccount().getName()).timestamp(m.getMessageDate())
            .build()).collect(Collectors.toList());
  }
}
