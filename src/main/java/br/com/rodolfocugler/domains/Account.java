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
  @JsonBackReference
  private AccountGroup accountGroup;

  @ManyToOne
  @JsonBackReference
  private Environment environment;

  @OneToMany(mappedBy = "account")
  @JsonManagedReference
  private List<Response> responses;
}