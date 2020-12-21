package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.Account;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.services.AccountService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  private final AccountService accountService;

  @GetMapping("/{id}")
  public Account get(@PathVariable long id) throws DataNotFoundException {
    return accountService.get(id);
  }

  @GetMapping
  public List<Account> get() {
    return accountService.get();
  }

  @PostMapping
  public Account add(@RequestBody @Validated Account account) {
    return accountService.add(account);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) throws DataNotFoundException {
    accountService.delete(id);
  }

  @PutMapping("/{id}")
  public void edit(@PathVariable long id, @RequestBody @Validated Account newAccount)
          throws DataNotFoundException {
    accountService.edit(id, newAccount);
  }
}
