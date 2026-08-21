package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record EstimateItemRequest(

        @NotBlank(message = "Pole nie może być puste")
        @Size(min = 1, max = 200, message = "Nazwa może mieć maksymalnie 200 znaków")
        String name,
        @NotBlank(message = "Pole nie może być puste") String itemType,
        @NotBlank(message = "Pole nie może być puste") String unit,
        @NotNull BigDecimal quantity,
        @NotNull BigDecimal pricePerUnit,
        BigDecimal pricePerHour
        ) {
}
