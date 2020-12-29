package br.com.rodolfocugler.repositories;

import br.com.rodolfocugler.domains.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EnvironmentRepository extends JpaRepository<Environment, Long> {

  @Query(value = "from Environment e " +
          "left join Question q on e.id = :id and q.environment.id = e.id " +
          "left join Response r on q.id = r.question.id and r.account.id = :userId")
  Environment findWithUserResponses(long id, long userId);
}