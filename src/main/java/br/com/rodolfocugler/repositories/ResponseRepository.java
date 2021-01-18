package br.com.rodolfocugler.repositories;

import br.com.rodolfocugler.domains.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface ResponseRepository extends JpaRepository<Response, Long> {
}