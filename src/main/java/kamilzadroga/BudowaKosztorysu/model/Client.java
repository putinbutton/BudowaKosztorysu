package kamilzadroga.BudowaKosztorysu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;
// czy możemy bez problemu usunąć w modelu @not blank email (email jest zbędny często)
    @NotBlank
    @Email
    private String email;

    @OneToMany(mappedBy = "client")
    private List<Project> projectList = new ArrayList<>();

}
