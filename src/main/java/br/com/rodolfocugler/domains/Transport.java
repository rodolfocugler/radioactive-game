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
public class Transport {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id = 0;

  @ManyToOne
  private AccountGroup accountGroup;

  @Column(nullable = false)
  private int carIndex;

  @ManyToMany
  private List<Account> accounts;

  @ManyToMany
  private List<Tool> tools;

  @Column(nullable = false)
  private long timestamp;

  @ManyToOne
  private Environment fromEnvironment;

  @ManyToOne
  private Environment toEnvironment;
}