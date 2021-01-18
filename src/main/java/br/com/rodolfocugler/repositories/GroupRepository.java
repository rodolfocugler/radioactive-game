package br.com.rodolfocugler.repositories;

import br.com.rodolfocugler.domains.AccountGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface GroupRepository extends JpaRepository<AccountGroup, Long> {
}