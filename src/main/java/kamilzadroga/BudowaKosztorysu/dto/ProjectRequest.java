package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ProjectRequest(
        @NotNull Long clientId,
        @NotBlank String name,
        @NotNull LocalDate creationDate

        ) {
}
