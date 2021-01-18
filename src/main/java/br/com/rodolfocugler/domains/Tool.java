package br.com.rodolfocugler.domains;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Tool {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id = 0;

  @Column(nullable = false)
  private String description;

  @Column
  private long accountGroupId;

  @ManyToOne
  @JsonIgnoreProperties("tools")
  private Environment environment;
}