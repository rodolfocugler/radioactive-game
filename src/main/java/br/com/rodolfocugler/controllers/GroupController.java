package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.Group;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.services.GroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
  public GroupController(GroupService groupService) {
    this.groupService = groupService;
  }

  private final GroupService groupService;

  @GetMapping("/{id}")
  public Group get(@PathVariable long id) throws DataNotFoundException {
    return groupService.get(id);
  }

  @GetMapping
  public List<Group> get() {
    return groupService.get();
  }

  @PostMapping
  public Group add(@RequestBody @Validated Group group) {
    return groupService.add(group);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) throws DataNotFoundException {
    groupService.delete(id);
  }

  @PutMapping("/{id}")
  public void edit(@PathVariable long id, @RequestBody @Validated Group newGroup)
          throws DataNotFoundException {
    groupService.edit(id, newGroup);
  }
}
