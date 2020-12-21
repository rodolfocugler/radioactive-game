package br.com.rodolfocugler.repositories;

import br.com.rodolfocugler.domains.Account;
import br.com.rodolfocugler.domains.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
}