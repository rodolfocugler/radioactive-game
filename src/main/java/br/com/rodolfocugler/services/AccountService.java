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
    return accountRepository.findAll();
  }

  public Account add(Account account) {
    account.setNumber(bCryptPasswordEncoder.encode(account.getNumber()));
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
    Account account = accountRepository.findByEmail(email);

    if (account == null) {
      throw new UsernameNotFoundException(email);
    }


    return new User(account.getEmail() + ";" + account.getId(), account.getNumber(), emptyList());
  }
}