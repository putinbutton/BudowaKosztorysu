package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record EstimateItemRequest(
        @NotBlank(message = "Pole nie może być puste") String name,
        @NotBlank(message = "Pole nie może być puste") String itemType,
        @NotBlank(message = "Pole nie może być puste") String unit,
        @NotNull BigDecimal quantity,
        @NotNull BigDecimal pricePerUnit,
        BigDecimal pricePerHour
        ) {
}
