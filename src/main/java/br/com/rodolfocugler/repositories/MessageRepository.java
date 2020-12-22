package br.com.rodolfocugler.repositories;

import br.com.rodolfocugler.domains.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
}