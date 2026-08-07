package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;

public record EstimateRequest(
        @NotNull Long projectId,
        @NotNull LocalDate creationDate
        ) {
}
