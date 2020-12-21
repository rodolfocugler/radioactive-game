package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.Tool;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.services.ToolService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tools")
public class ToolController {
  public ToolController(ToolService toolService) {
    this.toolService = toolService;
  }

  private final ToolService toolService;

  @GetMapping("/{id}")
  public Tool get(@PathVariable long id) throws DataNotFoundException {
    return toolService.get(id);
  }

  @GetMapping
  public List<Tool> get() {
    return toolService.get();
  }

  @PostMapping
  public Tool add(@RequestBody @Validated Tool tool) {
    return toolService.add(tool);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) throws DataNotFoundException {
    toolService.delete(id);
  }

  @PutMapping("/{id}")
  public void edit(@PathVariable long id, @RequestBody @Validated Tool newTool)
          throws DataNotFoundException {
    toolService.edit(id, newTool);
  }
}
