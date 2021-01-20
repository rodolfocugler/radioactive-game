package br.com.rodolfocugler.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id = 0;

  @Column(nullable = false, length=2048)
  private String description;

  @ManyToOne
  @JsonIgnoreProperties("questions")
  private Environment environment;

  @OneToMany(mappedBy = "question")
  @JsonIgnoreProperties(value = {"question"}, allowSetters = true)
  private List<Response> responses;
}