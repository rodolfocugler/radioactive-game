package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.AccountGroup;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountGroupService {

  public AccountGroupService(GroupRepository groupRepository) {
    this.groupRepository = groupRepository;
  }

  private final GroupRepository groupRepository;

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