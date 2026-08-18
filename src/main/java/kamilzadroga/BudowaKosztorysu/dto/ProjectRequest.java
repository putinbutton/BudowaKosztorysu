package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ProjectRequest(
        @NotNull Long clientId,
        @NotBlank(message = "Pole nie może być puste") String name,
        @NotNull LocalDate creationDate

        ) {
}
