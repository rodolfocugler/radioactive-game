package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.AccountGroup;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.services.AccountGroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accountGroups")
public class AccountGroupController {
  public AccountGroupController(AccountGroupService accountGroupService) {
    this.accountGroupService = accountGroupService;
  }

  private final AccountGroupService accountGroupService;

  @GetMapping("/{id}")
  public AccountGroup get(@PathVariable long id) throws DataNotFoundException {
    return accountGroupService.get(id);
  }

  @GetMapping
  public List<AccountGroup> get() {
    return accountGroupService.get();
  }

  @PostMapping
  public AccountGroup add(@RequestBody @Validated AccountGroup accountGroup) {
    return accountGroupService.add(accountGroup);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) throws DataNotFoundException {
    accountGroupService.delete(id);
  }

  @PutMapping("/{id}")
  public void edit(@PathVariable long id, @RequestBody @Validated AccountGroup newAccountGroup)
          throws DataNotFoundException {
    accountGroupService.edit(id, newAccountGroup);
  }
}
