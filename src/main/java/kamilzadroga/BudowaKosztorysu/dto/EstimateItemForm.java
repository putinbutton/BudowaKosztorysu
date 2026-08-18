package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class EstimateItemForm {

    @NotBlank(message = "Pole nie może być puste")
    private String name;

    @NotBlank(message = "Pole nie może być puste")
    private String itemType;

    @NotBlank(message = "Pole nie może być puste")
    private String unit;

    @NotNull
    private BigDecimal quantity;

    @NotNull
    private BigDecimal pricePerUnit;

    private BigDecimal pricePerHour;
}
