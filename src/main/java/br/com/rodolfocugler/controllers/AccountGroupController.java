package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.AccountGroup;
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
  public AccountGroup get(@PathVariable long id) throws DataNotFoundException {
    return groupService.get(id);
  }

  @GetMapping
  public List<AccountGroup> get() {
    return groupService.get();
  }

  @PostMapping
  public AccountGroup add(@RequestBody @Validated AccountGroup accountGroup) {
    return groupService.add(accountGroup);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) throws DataNotFoundException {
    groupService.delete(id);
  }

  @PutMapping("/{id}")
  public void edit(@PathVariable long id, @RequestBody @Validated AccountGroup newAccountGroup)
          throws DataNotFoundException {
    groupService.edit(id, newAccountGroup);
  }
}
