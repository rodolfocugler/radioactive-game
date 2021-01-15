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
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id = 0;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String number;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private boolean isLeader;

  @ManyToOne
  @JsonIgnoreProperties("accounts")
  private AccountGroup accountGroup;

  @ManyToOne
  @JsonIgnoreProperties("accounts")
  private Environment environment;

  @OneToMany(mappedBy = "account")
  @JsonIgnoreProperties("account")
  private List<Response> responses;

//  @OneToMany(mappedBy = "account")
//  @JsonIgnoreProperties(value = {"account"}, allowSetters = true)
//  private List<ChatMessage> chatMessages;
}