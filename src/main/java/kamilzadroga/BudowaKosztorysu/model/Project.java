package kamilzadroga.BudowaKosztorysu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor

public class Project extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private Client client;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate creationDate;

    @OneToOne(mappedBy = "project")
    private Estimate estimate;

}
