package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.Account;
import br.com.rodolfocugler.domains.ChatMessage;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.services.MessageService;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chatMessages")
public class ChatMessageController {
  public ChatMessageController(MessageService messageService,
                               SimpMessagingTemplate simpMessagingTemplate) {
    this.messageService = messageService;
    this.simpMessagingTemplate = simpMessagingTemplate;
  }

  private final MessageService messageService;
  private final SimpMessagingTemplate simpMessagingTemplate;

  @GetMapping("/{id}")
  public ChatMessage get(@PathVariable long id) throws DataNotFoundException {
    return messageService.get(id);
  }

  @GetMapping
  public List<ChatMessage> get() {
    return messageService.get();
  }

  @GetMapping("/getByEnvironmentId/{id}")
  public List<ChatMessage> getByEnvironmentId(@PathVariable long id) {
    return messageService.getByEnvironmentId(id);
  }

  @PostMapping
  @SendTo("/topic/messages")
  public ChatMessage add(@RequestBody @Validated ChatMessage chatMessage) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    long userId = Long.parseLong(authentication.getPrincipal().toString());
    chatMessage.setAccount(Account.builder().id(userId).build());
    chatMessage.setMessageDate(LocalDate.now().atStartOfDay()
            .toInstant(OffsetDateTime.now().getOffset()).toEpochMilli());

    return messageService.add(chatMessage);
  }

  @GetMapping("/hello")
  public void greeting() throws InterruptedException {
    Thread.sleep(1000); // simulated delay
    simpMessagingTemplate.convertAndSend("/topic/greetings",
            ChatMessage.builder().text("ola").build());
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) throws DataNotFoundException {
    messageService.delete(id);
  }
}
