package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.Account;
import br.com.rodolfocugler.domains.AccountGroup;
import br.com.rodolfocugler.domains.Environment;
import br.com.rodolfocugler.domains.Transport;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.repositories.AccountRepository;
import br.com.rodolfocugler.repositories.GroupRepository;
import br.com.rodolfocugler.repositories.TransportRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountGroupService {

  public AccountGroupService(GroupRepository groupRepository,
                             AccountRepository accountRepository,
                             TransportRepository transportRepository) {
    this.groupRepository = groupRepository;
    this.accountRepository = accountRepository;
    this.transportRepository = transportRepository;
  }

  private final GroupRepository groupRepository;
  private final AccountRepository accountRepository;
  private final TransportRepository transportRepository;

  public AccountGroup get(long id) throws DataNotFoundException {
    return groupRepository
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("Group was not found."));
  }

  public List<AccountGroup> get() {
    return groupRepository.findAll();
  }

  public AccountGroup add(AccountGroup accountGroup) {
    groupRepository.save(accountGroup);
    accountGroup.getAccounts().parallelStream().forEach(a -> {
      Account account = accountRepository.findById(a.getId())
              .orElseThrow();
      account.setAccountGroup(accountGroup);
      accountRepository.save(account);
    });

    transportRepository.save(generateTransport(accountGroup, 1));
    transportRepository.save(generateTransport(accountGroup, 2));

    return accountGroup;
  }

  private Transport generateTransport(AccountGroup accountGroup, int carIndex) {
    return Transport.builder().accountGroup(accountGroup)
            .accounts(accountGroup.getAccounts())
            .toEnvironment(Environment.builder().id(1).build())
            .fromEnvironment(Environment.builder().id(1).build())
            .carIndex(carIndex)
            .timestamp(System.currentTimeMillis())
            .build();
  }

  public AccountGroup edit(long id, AccountGroup newAccountGroup) throws DataNotFoundException {
    AccountGroup accountGroup = get(id);
    accountGroup.setName(newAccountGroup.getName());
    return groupRepository.save(accountGroup);
  }

  public void delete(long id) throws DataNotFoundException {
    AccountGroup accountGroup = get(id);
    groupRepository.delete(accountGroup);
  }
}