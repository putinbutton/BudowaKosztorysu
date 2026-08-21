package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ProjectRequest(
        @NotNull Long clientId,
        @NotBlank(message = "Pole nie może być puste")
        @Size(min = 1, max = 200, message = "Nazwa/adres budowy może mieć maksymalnie 200 znaków")
        String name,
        @NotNull LocalDate creationDate

        ) {
}
