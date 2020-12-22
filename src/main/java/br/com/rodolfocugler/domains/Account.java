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
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id = 0;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private String number;

  @ManyToOne
  private AccountGroup accountGroup;

  @ManyToOne
  private Environment environment;

  @OneToMany(mappedBy = "account")
  private List<Response> responses;
}