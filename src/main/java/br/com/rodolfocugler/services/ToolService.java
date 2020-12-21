package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.Tool;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.repositories.ToolRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToolService {

  public ToolService(ToolRepository toolRepository) {
    this.toolRepository = toolRepository;
  }

  private final ToolRepository toolRepository;

  public Tool get(long id) throws DataNotFoundException {
    return toolRepository
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("Tool was not found."));
  }

  public List<Tool> get() {
    return toolRepository.findAll();
  }

  public Tool add(Tool tool) {
    toolRepository.save(tool);
    return tool;
  }

  public Tool edit(long id, Tool newTool) throws DataNotFoundException {
    Tool tool = get(id);
    tool.setDescription(newTool.getDescription());
    tool.setEnvironment(newTool.getEnvironment());
    return toolRepository.save(tool);
  }

  public void delete(long id) throws DataNotFoundException {
    Tool tool = get(id);
    toolRepository.delete(tool);
  }
}