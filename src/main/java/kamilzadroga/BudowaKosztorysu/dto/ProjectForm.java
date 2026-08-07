package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ProjectForm {

    @NotNull
    private Long clientId;

    @NotBlank
    private String name;

    @NotNull
    private LocalDate creationDate;
}
