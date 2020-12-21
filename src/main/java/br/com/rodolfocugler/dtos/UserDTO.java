package br.com.rodolfocugler.dtos;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
  private String email;
  private String password;
}
