package br.com.rodolfocugler.services;

import br.com.rodolfocugler.domains.Question;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.repositories.QuestionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {

  public QuestionService(QuestionRepository questionRepository) {
    this.questionRepository = questionRepository;
  }

  private final QuestionRepository questionRepository;

  public Question get(long id) throws DataNotFoundException {
    return questionRepository
            .findById(id)
            .orElseThrow(() -> new DataNotFoundException("Question was not found."));
  }

  public List<Question> get() {
    return questionRepository.findAll();
  }

  public Question add(Question question) {
    questionRepository.save(question);
    return question;
  }

  public Question edit(long id, Question newQuestion) throws DataNotFoundException {
    Question question = get(id);
    question.setDescription(newQuestion.getDescription());
    question.setEnvironment(newQuestion.getEnvironment());
    return questionRepository.save(question);
  }

  public void delete(long id) throws DataNotFoundException {
    Question question = get(id);
    questionRepository.delete(question);
  }
}