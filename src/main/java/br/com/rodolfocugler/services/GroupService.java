package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.Group;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

  public GroupService(GroupRepository groupRepository) {
    this.groupRepository = groupRepository;
  }

  private final GroupRepository groupRepository;

  public Group get(long id) throws DataNotFoundException {
    return groupRepository
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("Group was not found."));
  }

  public List<Group> get() {
    return groupRepository.findAll();
  }

  public Group add(Group group) {
    groupRepository.save(group);
    return group;
  }

  public Group edit(long id, Group newGroup) throws DataNotFoundException {
    Group group = get(id);
    group.setName(newGroup.getName());
    return groupRepository.save(group);
  }

  public void delete(long id) throws DataNotFoundException {
    Group group = get(id);
    groupRepository.delete(group);
  }
}