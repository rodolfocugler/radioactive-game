package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.Account;
import br.com.rodolfocugler.domains.Tool;
import br.com.rodolfocugler.domains.Transport;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.repositories.AccountRepository;
import br.com.rodolfocugler.repositories.ToolRepository;
import br.com.rodolfocugler.repositories.TransportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TransportService {

  public TransportService(TransportRepository transportRepository,
                          ToolRepository toolRepository,
                          AccountRepository accountRepository) {
    this.transportRepository = transportRepository;
    this.toolRepository = toolRepository;
    this.accountRepository = accountRepository;
  }

  private final TransportRepository transportRepository;
  private final ToolRepository toolRepository;
  private final AccountRepository accountRepository;

  public Transport get(long id) throws DataNotFoundException {
    return transportRepository
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("Transport was not found."));
  }

  public List<Transport> get() {
    return transportRepository.findAll();
  }

  public Transport add(Transport transport) {
    Map<String, Tool> tools = toolRepository
            .findAllByEnvironment_IdAndAccountGroupId(transport.getFromEnvironment().getId(),
                    transport.getAccountGroup().getId())
            .stream().collect(Collectors.toMap(Tool::getDescription, e -> e));

    transport.getTools().stream().map(tool -> {
      String description = tool.getDescription().trim();

      if (tools.containsKey(description)) {
        Tool toolDb = tools.get(description);
        toolDb.setEnvironment(transport.getToEnvironment());
        toolRepository.save(toolDb);
        return toolDb;
      } else if (transport.getFromEnvironment().getId() == 1) {
        tool.setDescription(description);
        tool.setEnvironment(transport.getToEnvironment());
        tool.setAccountGroupId(transport.getAccountGroup().getId());
        toolRepository.save(tool);
        return tool;
      }
      return null;
    }).filter(Objects::nonNull).collect(Collectors.toList());
    transport.getAccounts().forEach(account -> {
      Account accountDb = accountRepository.findById(account.getId()).get();
      accountDb.setEnvironment(transport.getToEnvironment());
      accountRepository.save(accountDb);
    });
    transportRepository.save(transport);
    return transport;
  }

  public Transport getByIndex(int carId, long accountGroupId) {
    Transport transport = transportRepository
            .findFirstByAccountGroup_IdAndCarIndexOrderByTimestampDesc(accountGroupId, carId);
    removeFields(transport);
    return transport;
  }

  private void removeFields(Transport transport) {
    transport.getAccounts().parallelStream().forEach(account ->
            account.getEnvironment().setQuestions(null));
    transport.getAccountGroup().setAccounts(null);
    transport.getFromEnvironment().setQuestions(null);
    transport.getToEnvironment().setQuestions(null);
  }

  public List<Transport> getByAccountGroupId(long accountGroupId) {
    List<Transport> transports = transportRepository
            .findAllByAccountGroup_IdOrderByTimestamp(accountGroupId);

    transports.parallelStream().forEach(this::removeFields);
    return transports;
  }
}