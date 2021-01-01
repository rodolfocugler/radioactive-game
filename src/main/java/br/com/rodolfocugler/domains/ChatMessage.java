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
public class ChatMessage {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id = 0;

  @Column(nullable = false, length=2048)
  private String text;

  @Column(nullable = false)
  private long messageDate;

  @ManyToOne
  @JsonIgnoreProperties("chatMessages")
  private Environment environment;

  @ManyToOne
  @JsonIgnoreProperties(value = {"chatMessages"}, allowSetters = true)
  private Account account;
}