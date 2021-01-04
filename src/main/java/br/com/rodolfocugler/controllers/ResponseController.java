package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.Account;
import br.com.rodolfocugler.domains.Response;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.services.ResponseService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/responses")
public class ResponseController {
  public ResponseController(ResponseService responseService) {
    this.responseService = responseService;
  }

  private final ResponseService responseService;

  @GetMapping("/{id}")
  public Response get(@PathVariable long id) throws DataNotFoundException {
    return responseService.get(id);
  }

  @GetMapping
  public List<Response> get() {
    return responseService.get();
  }

  @PostMapping
  public Response add(@RequestBody @Validated Response response) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Account logged = (Account) authentication.getPrincipal();
    response.setAccount(logged);
    return responseService.add(response);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) throws DataNotFoundException {
    responseService.delete(id);
  }

  @PutMapping("/{id}")
  public void edit(@PathVariable long id, @RequestBody @Validated Response newResponse)
          throws DataNotFoundException {
    responseService.edit(id, newResponse);
  }
}
