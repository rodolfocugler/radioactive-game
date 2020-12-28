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
public class Transport {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id = 0;

  @OneToMany
  private List<Account> accounts;

  @OneToMany
  private List<Tool> tools;

  @Column(nullable = false)
  private long timestamp;
}