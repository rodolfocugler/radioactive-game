package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.Environment;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.services.EnvironmentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/environments")
public class EnvironmentController {
  public EnvironmentController(EnvironmentService environmentService) {
    this.environmentService = environmentService;
  }

  private final EnvironmentService environmentService;

  @GetMapping("/{id}")
  public Environment get(@PathVariable long id) throws DataNotFoundException {
    return environmentService.get(id);
  }

  @GetMapping("/getWithUserResponses/{id}")
  public Environment getWithUserResponses(@RequestHeader("Authorization") String authorization,
                                          @PathVariable long id) throws DataNotFoundException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    long userId = Long.parseLong(authentication.getPrincipal().toString());
    return environmentService.getWithUserResponses(id, userId);
  }

  @GetMapping
  public List<Environment> get() {
    return environmentService.get();
  }

  @PostMapping
  public Environment add(@RequestBody @Validated Environment environment) {
    return environmentService.add(environment);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) throws DataNotFoundException {
    environmentService.delete(id);
  }

  @PutMapping("/{id}")
  public void edit(@PathVariable long id, @RequestBody @Validated Environment newEnvironment)
          throws DataNotFoundException {
    environmentService.edit(id, newEnvironment);
  }
}
