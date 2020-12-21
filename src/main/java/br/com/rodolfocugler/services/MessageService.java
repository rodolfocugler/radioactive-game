package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.Message;
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

  public Message get(long id) throws DataNotFoundException {
    return messageRepository
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("Message was not found."));
  }

  public List<Message> get() {
    return messageRepository.findAll();
  }

  public Message add(Message message) {
    messageRepository.save(message);
    return message;
  }

  public void delete(long id) throws DataNotFoundException {
    Message message = get(id);
    messageRepository.delete(message);
  }
}