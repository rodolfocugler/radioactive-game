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
public class Group {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id = 0;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "group")
  private List<Account> accounts;
}