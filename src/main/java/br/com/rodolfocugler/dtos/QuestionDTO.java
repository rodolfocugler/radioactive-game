package br.com.rodolfocugler.dtos;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionDTO {
  private String question;
  private List<ResponseDTO> responses;
}
