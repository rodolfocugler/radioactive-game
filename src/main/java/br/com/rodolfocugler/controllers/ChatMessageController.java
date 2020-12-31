package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.Account;
import br.com.rodolfocugler.domains.ChatMessage;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.services.MessageService;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatMessages")
public class ChatMessageController {
  public ChatMessageController(MessageService messageService) {
    this.messageService = messageService;
  }

  private final MessageService messageService;

  @GetMapping("/{id}")
  public ChatMessage get(@PathVariable long id) throws DataNotFoundException {
    return messageService.get(id);
  }

  @GetMapping
  public List<ChatMessage> get() {
    return messageService.get();
  }

  @PostMapping
  @SendTo("/topic/messages")
  public ChatMessage add(@RequestBody @Validated ChatMessage chatMessage) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    long userId = Long.parseLong(authentication.getPrincipal().toString());
    chatMessage.setAccount(Account.builder().id(userId).build());
    return messageService.add(chatMessage);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) throws DataNotFoundException {
    messageService.delete(id);
  }
}
