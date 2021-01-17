package br.com.rodolfocugler.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id = 0;

  @Column(nullable = false, length = 1024)
  private String text;

  @Column(nullable = false)
  private long timestamp;

  @ManyToOne
  @JsonIgnoreProperties(value = {"responses"}, allowSetters = true)
  private Question question;

  @ManyToOne
  @JsonIgnoreProperties(value = {"responses"}, allowSetters = true)
  private Account account;
}