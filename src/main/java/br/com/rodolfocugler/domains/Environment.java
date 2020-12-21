package br.com.rodolfocugler.domains;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Environment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id = 0;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @OneToMany(mappedBy = "environment")
  private List<Account> accounts;

  @OneToMany(mappedBy = "environment")
  private List<Question> questions;

  @OneToMany(mappedBy = "environment")
  private List<Message> messages;

  @OneToMany(mappedBy = "environment")
  private List<Tool> tools;
}