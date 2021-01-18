package br.com.rodolfocugler.repositories;

import br.com.rodolfocugler.domains.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
}