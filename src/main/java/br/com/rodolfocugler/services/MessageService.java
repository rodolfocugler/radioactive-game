package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.ChatMessage;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.repositories.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

  public MessageService(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  private final MessageRepository messageRepository;

  public ChatMessage get(long id) throws DataNotFoundException {
    return messageRepository
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("Message was not found."));
  }

  public List<ChatMessage> get() {
    return messageRepository.findAll();
  }

  public ChatMessage add(ChatMessage chatMessage) {
    chatMessage.setText(chatMessage.getText().replace("\n", "<br/>"));
    messageRepository.save(chatMessage);
    return chatMessage;
  }

  public void delete(long id) throws DataNotFoundException {
    ChatMessage chatMessage = get(id);
    messageRepository.delete(chatMessage);
  }

  public List<ChatMessage> getByEnvironmentId(long id) {
    return messageRepository.findAllByEnvironment_IdOrderByMessageDate(id);
  }
}