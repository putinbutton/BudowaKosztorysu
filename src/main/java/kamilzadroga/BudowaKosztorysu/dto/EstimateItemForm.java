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

    @NotBlank
    private String name;

    @NotBlank
    private String itemType;

    @NotBlank
    private String unit;

    @NotNull
    private BigDecimal quantity;

    @NotNull
    private BigDecimal pricePerUnit;

    private BigDecimal pricePerHour;
}
