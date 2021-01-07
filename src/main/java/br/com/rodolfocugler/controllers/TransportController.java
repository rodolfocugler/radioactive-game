package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.Transport;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.services.TransportService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transports")
public class TransportController {
  public TransportController(TransportService transportService) {
    this.transportService = transportService;
  }

  private final TransportService transportService;

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
    return transportService.add(transport);
  }
}
