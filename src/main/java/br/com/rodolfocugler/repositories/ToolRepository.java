package br.com.rodolfocugler.repositories;

import br.com.rodolfocugler.domains.Tool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolRepository extends JpaRepository<Tool, Long> {
  List<Tool> findAllByEnvironment_IdAndAccountGroupId(long environmentId, long accountGroupId);
}