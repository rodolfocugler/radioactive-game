package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.Account;
import br.com.rodolfocugler.domains.AccountGroup;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.repositories.AccountRepository;
import br.com.rodolfocugler.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountGroupService {

  public AccountGroupService(GroupRepository groupRepository,
                             AccountRepository accountRepository) {
    this.groupRepository = groupRepository;
    this.accountRepository = accountRepository;
  }

  private final GroupRepository groupRepository;
  private final AccountRepository accountRepository;

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

    return accountGroup;
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