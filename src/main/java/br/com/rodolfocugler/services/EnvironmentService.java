package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.Environment;
import br.com.rodolfocugler.domains.Question;
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
    Environment environment = environmentRepository
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("Environment was not found."));

    removeEnvironmentProperties(environment);
    return environment;
  }

  private void removeEnvironmentProperties(Environment environment) {
    List<Question> questions = environment.getQuestions();
    if (questions != null) {
      questions.forEach(question -> {
        question.getResponses()
                .parallelStream()
                .forEach(response -> {
                  response.getAccount().setEnvironment(null);
                  response.getAccount().setResponses(null);
//                response.getAccount().setChatMessages(null);
                  response.setQuestion(null);
                });
        question.setEnvironment(null);
      });
    }
//    environment.setAccounts(null);
//    environment.setChatMessages(null);
  }

  public List<Environment> get() {
    List<Environment> environments = environmentRepository.findAllByOrderByIdAsc();
    environments.parallelStream()
            .forEach(this::removeEnvironmentProperties);
    return environments;
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
      List<Response> responses = question.getResponses().parallelStream()
              .filter(response -> response.getAccount().getId() == userId)
              .collect(Collectors.toList());

      responses.forEach(response -> response.setAccount(null));
      question.setResponses(responses);
      question.setEnvironment(null);
    });

    return environment;
  }

  public List<Environment> getByAccountGroup(long accountGroupId) {
    List<Environment> environments = environmentRepository.findAllByOrderByIdAsc();

    environments.forEach(environment -> {
      List<Question> questions = environment.getQuestions();
      if (questions != null) {
        questions.forEach(question -> {
          List<Response> responses = question.getResponses().parallelStream()
                  .filter(response ->
                          response.getAccount().getAccountGroup().getId() == accountGroupId)
                  .collect(Collectors.toList());
          responses.forEach(response -> response.setAccount(null));
          question.setResponses(responses);
          question.setEnvironment(null);
        });
      }
    });

    return environments;
  }
}