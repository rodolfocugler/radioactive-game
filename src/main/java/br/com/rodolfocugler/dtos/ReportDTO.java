package br.com.rodolfocugler.dtos;


import br.com.rodolfocugler.domains.ChatMessage;
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
  private Map<String, List<ChatMessage>> messages;
  private long startTime;
  private long endTime;
  private long duration;
}
