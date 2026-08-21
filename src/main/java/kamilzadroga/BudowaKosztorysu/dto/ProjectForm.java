package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Pole nie może być puste")
    @Size(min = 1, max = 200, message = "Nazwa/adres budowy może mieć maksymalnie 200 znaków")
    private String name;

    @NotNull
    private LocalDate creationDate;
}
