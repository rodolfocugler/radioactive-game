package br.com.rodolfocugler.domains;

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
public class Environment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id = 0;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String icon;

  @OneToMany(mappedBy = "environment")
  @JsonManagedReference
  private List<Account> accounts;

  @OneToMany(mappedBy = "environment")
  @JsonManagedReference
  private List<Question> questions;

  @OneToMany(mappedBy = "environment")
  @JsonManagedReference
  private List<ChatMessage> chatMessages;

  @OneToMany(mappedBy = "environment")
  @JsonManagedReference
  private List<Tool> tools;
}