package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.Account;
import br.com.rodolfocugler.domains.Transport;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.services.TransportService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transports")
public class TransportController {
  public TransportController(TransportService transportService,
                             SimpMessagingTemplate simpMessagingTemplate) {
    this.transportService = transportService;
    this.simpMessagingTemplate = simpMessagingTemplate;
  }

  private final TransportService transportService;
  private final SimpMessagingTemplate simpMessagingTemplate;

  @GetMapping("/{id}")
  public Transport get(@PathVariable long id) throws DataNotFoundException {
    return transportService.get(id);
  }

  @GetMapping("/getByIndex/{carId}/{accountGroupId}")
  public Transport getByIndex(@PathVariable int carId, @PathVariable long accountGroupId) {
    return transportService.getByIndex(carId, accountGroupId);
  }

  @GetMapping
  public List<Transport> get() {
    return transportService.get();
  }

  @PostMapping
  public Transport add(@RequestBody @Validated Transport transport) {
    transport.setTimestamp(LocalDate.now().atStartOfDay()
            .toInstant(OffsetDateTime.now().getOffset()).toEpochMilli());
    Transport transportSaved = transportService.add(transport);

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Account logged = (Account) authentication.getPrincipal();

    simpMessagingTemplate.convertAndSend("/topic/transport/" +
            + logged.getAccountGroup().getId(), transport);

    return transportSaved;
  }
}
