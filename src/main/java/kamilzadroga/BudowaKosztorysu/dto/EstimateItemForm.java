package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class EstimateItemForm {

    @NotBlank(message = "Pole nie może być puste")
    @Size(min = 1, max = 200, message = "Nazwa może mieć maksymalnie 200 znaków")
    private String name;

    @NotBlank(message = "Pole nie może być puste")
    @Size(max = 50)
    private String itemType;

    @NotBlank(message = "Pole nie może być puste")
    @Size(max = 50)
    private String unit;

    @NotNull
    private BigDecimal quantity;

    @NotNull
    private BigDecimal pricePerUnit;

    private BigDecimal pricePerHour;
}
