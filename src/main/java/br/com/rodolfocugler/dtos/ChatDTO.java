package br.com.rodolfocugler.dtos;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDTO {
  private String accounts;
  private String text;
  private long timestamp;
}
