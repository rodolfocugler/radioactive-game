package br.com.rodolfocugler.dtos;


import lombok.*;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {
  private Map<String, String> accounts;
  private List<? extends EventDTO> events;
}
