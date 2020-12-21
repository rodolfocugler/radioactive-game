package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.Message;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.services.MessageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
  public MessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  private final MessageService messageService;

  @GetMapping("/{id}")
  public Message get(@PathVariable long id) throws DataNotFoundException {
    return messageService.get(id);
  }

  @GetMapping
  public List<Message> get() {
    return messageService.get();
  }

  @PostMapping
  public Message add(@RequestBody @Validated Message message) {
    return messageService.add(message);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) throws DataNotFoundException {
    messageService.delete(id);
  }
}
