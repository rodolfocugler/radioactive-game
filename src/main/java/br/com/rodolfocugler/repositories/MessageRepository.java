package br.com.rodolfocugler.repositories;

import br.com.rodolfocugler.domains.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
  List<ChatMessage> findAllByEnvironment_IdAndAccount_AccountGroup_IdOrderByMessageDate
          (Long environmentId, long accountGroupId);
}