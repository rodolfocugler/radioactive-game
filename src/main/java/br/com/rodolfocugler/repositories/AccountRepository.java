package br.com.rodolfocugler.repositories;

import br.com.rodolfocugler.domains.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface AccountRepository extends JpaRepository<Account, Long> {
  Account findByEmail(String email);

  List<Account> findAllByAccountGroup_IdOrderByName(long accountGroupId);

}