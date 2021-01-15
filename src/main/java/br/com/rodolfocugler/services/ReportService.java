package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.Account;
import br.com.rodolfocugler.domains.AccountGroup;
import br.com.rodolfocugler.domains.Tool;
import br.com.rodolfocugler.domains.Transport;
import br.com.rodolfocugler.dtos.EventDTO;
import br.com.rodolfocugler.dtos.ReportDTO;
import br.com.rodolfocugler.dtos.TransportEventDTO;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReportService {

  public ReportService(AccountGroupService accountGroupService,
                       TransportService transportService) {
    this.accountGroupService = accountGroupService;
    this.transportService = transportService;
  }

  private final AccountGroupService accountGroupService;
  private final TransportService transportService;

  public ReportDTO getByAccountGroupId(long accountGroupId) throws DataNotFoundException {
    AccountGroup accountGroup = accountGroupService.get(accountGroupId);
    Map<String, String> accounts = accountGroup.getAccounts().stream()
            .collect(Collectors.toMap(Account::getNumber, Account::getName));

    List<Transport> transports = transportService.getByAccountGroupId(accountGroupId);
    List<TransportEventDTO> transportEvents = transports.stream().map(t -> TransportEventDTO
            .builder().timestamp(t.getTimestamp()).accounts(new ArrayList<>(accounts.values()))
            .to(t.getToEnvironment().getName()).to(t.getToEnvironment().getName())
            .tools(t.getTools().stream().map(Tool::getDescription).collect(Collectors.toList()))
            .build()).collect(Collectors.toList());

    return ReportDTO.builder()
            .accounts(accounts)
            .events(transportEvents)
            .build();
  }
}
