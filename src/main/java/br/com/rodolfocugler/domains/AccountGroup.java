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
public class AccountGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id = 0;

  @Column(nullable = false)
  private String name;

  @OneToMany(mappedBy = "accountGroup")
  @JsonManagedReference
  private List<Account> accounts;
}