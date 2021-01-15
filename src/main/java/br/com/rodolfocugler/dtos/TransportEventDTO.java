package br.com.rodolfocugler.dtos;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransportEventDTO extends EventDTO {
  private String from;
  private String to;
  private List<String> accounts;
  private List<String> tools;
  private long timestamp;
}
