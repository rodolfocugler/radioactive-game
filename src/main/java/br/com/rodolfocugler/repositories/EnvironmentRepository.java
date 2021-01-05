package br.com.rodolfocugler.repositories;

import br.com.rodolfocugler.domains.Environment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnvironmentRepository extends JpaRepository<Environment, Long> {
  List<Environment> findAllByOrderByIdAsc();

  @Query(value = "select e.*, q.*, r.* from Environment e " +
          "left join Question q on e.id = :id and q.environment_id = e.id " +
          "left join Response r on q.id = r.question_id and r.account_id = :userId",
          nativeQuery = true)
  Environment findWithUserResponses(long id, long userId);
}