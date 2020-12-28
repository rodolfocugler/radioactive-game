package br.com.rodolfocugler.domains;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

  @Column(nullable = false)
  private String description;

  @ManyToOne
  @JsonBackReference
  private Environment environment;

  @OneToMany(mappedBy = "question")
  @JsonManagedReference
  private List<Response> responses;
}