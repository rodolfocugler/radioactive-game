package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.Environment;
import br.com.rodolfocugler.domains.Response;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.repositories.EnvironmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnvironmentService {

  public EnvironmentService(EnvironmentRepository environmentRepository) {
    this.environmentRepository = environmentRepository;
  }

  private final EnvironmentRepository environmentRepository;

  public Environment get(long id) throws DataNotFoundException {
    return environmentRepository
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("Environment was not found."));
  }

  public List<Environment> get() {
    return environmentRepository.findAll();
  }

  public Environment add(Environment environment) {
    environmentRepository.save(environment);
    return environment;
  }

  public Environment edit(long id, Environment newEnvironment) throws DataNotFoundException {
    Environment environment = get(id);
    environment.setName(newEnvironment.getName());
    environment.setIcon(newEnvironment.getIcon());
    environment.setDescription(newEnvironment.getDescription());
    return environmentRepository.save(environment);
  }

  public void delete(long id) throws DataNotFoundException {
    Environment environment = get(id);
    environmentRepository.delete(environment);
  }

  public Environment getWithUserResponses(long id, long userId) throws DataNotFoundException {
    Environment environment = get(id);
    environment.getQuestions().forEach(question -> {
      List<Response> responses = question.getResponses().stream()
              .filter(response -> response.getAccount().getId() == userId)
              .collect(Collectors.toList());
      question.setResponses(responses);
    });

    return environment;
  }
}