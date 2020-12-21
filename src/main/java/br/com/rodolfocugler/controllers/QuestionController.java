package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.domains.Question;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.services.QuestionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {
  public QuestionController(QuestionService questionService) {
    this.questionService = questionService;
  }

  private final QuestionService questionService;

  @GetMapping("/{id}")
  public Question get(@PathVariable long id) throws DataNotFoundException {
    return questionService.get(id);
  }

  @GetMapping
  public List<Question> get() {
    return questionService.get();
  }

  @PostMapping
  public Question add(@RequestBody @Validated Question question) {
    return questionService.add(question);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) throws DataNotFoundException {
    questionService.delete(id);
  }

  @PutMapping("/{id}")
  public void edit(@PathVariable long id, @RequestBody @Validated Question newQuestion)
          throws DataNotFoundException {
    questionService.edit(id, newQuestion);
  }
}
