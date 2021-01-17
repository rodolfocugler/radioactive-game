package br.com.rodolfocugler.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
  private String account;
  private String response;
  private long timestamp;
}
