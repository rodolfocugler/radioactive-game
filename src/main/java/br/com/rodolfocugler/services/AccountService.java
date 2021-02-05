package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.Account;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.repositories.AccountRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Collections.emptyList;

@Service
public class AccountService implements UserDetailsService {

  public AccountService(AccountRepository accountRepository,
                        BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.accountRepository = accountRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  private final AccountRepository accountRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public Account get(long id) throws DataNotFoundException {
    return accountRepository
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("Account was not found."));
  }

  public List<Account> get() {
    List<Account> accounts = accountRepository.findAll();
    accounts.parallelStream().forEach(account -> {
      account.setEnvironment(null);
      account.setResponses(null);
    });
    return accounts;
  }

  public Account add(Account account) {
    account.setLeader(false);
    account.setNumber(account.getNumber().trim());
    account.setEmail(account.getEmail().trim());
    account.setPassword(bCryptPasswordEncoder.encode(account.getNumber() + "@2021"));
    accountRepository.save(account);
    return account;
  }

  public Account edit(long id, Account newAccount) throws DataNotFoundException {
    Account account = get(id);
    account.setEmail(newAccount.getEmail());
    account.setNumber(newAccount.getNumber());
    account.setName(newAccount.getName());
    return accountRepository.save(account);
  }

  public void delete(long id) throws DataNotFoundException {
    Account account = get(id);
    accountRepository.delete(account);
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Account account = accountRepository.findByEmail(email.trim());

    if (account == null) {
      throw new UsernameNotFoundException(email.trim());
    }

    StringBuilder sb = new StringBuilder();
    sb.append(account.getEmail());
    sb.append(";");
    sb.append(account.getId());
    sb.append(";");
    sb.append(account.getName());
    sb.append(";");
    sb.append(account.getEnvironment() != null ? account.getEnvironment().getId() : 1);
    sb.append(";");
    sb.append(account.getAccountGroup() != null ? account.getAccountGroup().getId() : 1);
    sb.append(";");
    sb.append(account.isLeader());

    return new User(sb.toString(), account.getPassword(), emptyList());
  }

  public List<Account> getByGroupId(long accountGroupId) {
    return accountRepository.findAllByAccountGroup_IdOrderByName(accountGroupId);
  }
}