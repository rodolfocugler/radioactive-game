package br.com.rodolfocugler.repositories;

import br.com.rodolfocugler.domains.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
  Account findByEmail(String email);
}