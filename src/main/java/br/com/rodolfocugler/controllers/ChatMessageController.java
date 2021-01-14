package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.Account;
import br.com.rodolfocugler.domains.ChatMessage;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.services.MessageService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Account logged = (Account) authentication.getPrincipal();
    return messageService.getByEnvironmentId(id, logged.getAccountGroup().getId());
  }

  @GetMapping("/getByAccountGroup")
  public List<ChatMessage> getByAccountGroup() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Account logged = (Account) authentication.getPrincipal();
    return messageService.getByEnvironmentId(null, logged.getAccountGroup().getId());
  }

  @PostMapping
  public ChatMessage add(@RequestBody @Validated ChatMessage chatMessage) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Account logged = (Account) authentication.getPrincipal();

    chatMessage.setAccount(logged);
    chatMessage.setEnvironment(logged.getEnvironment());
    chatMessage.setMessageDate(System.currentTimeMillis());

    ChatMessage chatMessageSaved = messageService.add(chatMessage);

    simpMessagingTemplate.convertAndSend("/topic/messages/" +
            logged.getEnvironment().getId() + "/"
            + logged.getAccountGroup().getId(), chatMessageSaved);

    return chatMessageSaved;
  }

  @PostMapping("/all")
  public ChatMessage addAll(@RequestBody @Validated ChatMessage chatMessage) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Account logged = (Account) authentication.getPrincipal();

    chatMessage.setAccount(logged);
    chatMessage.setMessageDate(System.currentTimeMillis());

    ChatMessage chatMessageSaved = messageService.add(chatMessage);

    simpMessagingTemplate.convertAndSend("/topic/messages/" +
            + logged.getAccountGroup().getId(), chatMessageSaved);

    return chatMessageSaved;
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) throws DataNotFoundException {
    messageService.delete(id);
  }
}
