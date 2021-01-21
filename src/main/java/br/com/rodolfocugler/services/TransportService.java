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
import java.util.Objects;
import java.util.Optional;
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
    List<Tool> tools = toolRepository
            .findAllByEnvironment_IdAndAccountGroupId(transport.getFromEnvironment().getId(),
                    transport.getAccountGroup().getId());

    List<Tool> toolsDb = transport.getTools().stream().map(tool -> {
      String description = tool.getDescription().trim();
      Optional<Tool> optionalTool = tools.stream().filter(t -> t.getDescription().equals(description))
              .findFirst();

      if (optionalTool.isPresent()) {
        Tool toolDb = optionalTool.get();
        toolDb.setEnvironment(transport.getToEnvironment());
        toolRepository.save(toolDb);
        tools.remove(toolDb);
        return toolDb;
      } else if (transport.getFromEnvironment().getId() == 1 && !description.isEmpty()) {
        tool.setDescription(description);
        tool.setEnvironment(transport.getToEnvironment());
        tool.setAccountGroupId(transport.getAccountGroup().getId());
        toolRepository.save(tool);
        return tool;
      }
      return null;
    }).filter(Objects::nonNull).collect(Collectors.toList());

    transport.setTools(toolsDb);

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
    transport.getAccounts().parallelStream().forEach(account -> {
      if (account.getEnvironment() != null) account.getEnvironment().setQuestions(null);
    });
    if (transport.getAccountGroup() != null) transport.getAccountGroup().setAccounts(null);
    if (transport.getFromEnvironment() != null) transport.getFromEnvironment().setQuestions(null);
    if (transport.getToEnvironment() != null) transport.getToEnvironment().setQuestions(null);
  }

  public List<Transport> getByAccountGroupId(long accountGroupId) {
    List<Transport> transports = transportRepository
            .findAllByAccountGroup_IdOrderByTimestamp(accountGroupId);

    transports.parallelStream().forEach(this::removeFields);
    return transports;
  }
}