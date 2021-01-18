package br.com.rodolfocugler.controllers;

import br.com.rodolfocugler.dtos.QuestionDTO;
import br.com.rodolfocugler.dtos.ReportDTO;
import br.com.rodolfocugler.exceptions.DataNotFoundException;
import br.com.rodolfocugler.services.ReportService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportController {

  public ReportController(ReportService reportService) {
    this.reportService = reportService;
  }

  private final ReportService reportService;

  @GetMapping("/getByAccountGroupId/{accountGroupId}")
  public ReportDTO getByAccountGroupId(@PathVariable long accountGroupId) throws DataNotFoundException {
    return reportService.getByAccountGroupId(accountGroupId);
  }

  @GetMapping("/getQuestionsByAccountGroupId/{accountGroupId}")
  public Map<String, List<QuestionDTO>> getQuestionsByAccountGroupId(@PathVariable long accountGroupId) {
    return reportService.getQuestions(accountGroupId);
  }
}
