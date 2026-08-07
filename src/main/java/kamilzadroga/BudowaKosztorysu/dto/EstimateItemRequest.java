package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record EstimateItemRequest(
        @NotBlank String name,
        @NotBlank String itemType,
        @NotBlank String unit,
        @NotNull BigDecimal quantity,
        @NotNull BigDecimal pricePerUnit,
        BigDecimal pricePerHour
        ) {
}
