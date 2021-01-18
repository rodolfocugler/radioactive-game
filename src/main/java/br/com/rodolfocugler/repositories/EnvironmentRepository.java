package br.com.rodolfocugler.repositories;

import br.com.rodolfocugler.domains.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface EnvironmentRepository extends JpaRepository<Environment, Long> {
  List<Environment> findAllByOrderByIdAsc();
}