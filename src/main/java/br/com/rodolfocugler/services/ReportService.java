package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.*;
import br.com.rodolfocugler.dtos.ReportDTO;
import br.com.rodolfocugler.dtos.TransportEventDTO;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    AccountGroup accountGroup = accountGroupService.get(accountGroupId);
    Map<String, String> accounts = accountGroup.getAccounts().stream()
            .collect(Collectors.toMap(Account::getNumber, Account::getName));

    List<Transport> transports = transportService.getByAccountGroupId(accountGroupId);
    List<TransportEventDTO> transportEvents = transports.stream().map(t -> TransportEventDTO
            .builder().timestamp(t.getTimestamp()).accounts(new ArrayList<>(accounts.values()))
            .from(t.getFromEnvironment().getName()).to(t.getToEnvironment().getName())
            .tools(t.getTools().stream().map(Tool::getDescription).collect(Collectors.toList()))
            .build()).collect(Collectors.toList());

    List<Environment> environments = environmentService.get();
    Map<String, List<ChatMessage>> messages = environments.stream().collect(Collectors
            .toMap(Environment::getName,
                    e -> messageService.getByEnvironmentId(e.getId(), accountGroupId)));

    messages.put("Geral", messageService.getByEnvironmentId(null, accountGroupId));

    return ReportDTO.builder()
            .accounts(accounts)
            .startTime(transports.get(0).getTimestamp())
            .endTime(transports.get(transports.size() - 1).getTimestamp())
            .duration(transports.get(transports.size() - 1).getTimestamp()
                    - transports.get(0).getTimestamp())
            .messages(messages)
            .events(transportEvents)
            .build();
  }
}
