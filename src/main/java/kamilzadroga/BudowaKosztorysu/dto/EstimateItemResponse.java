package kamilzadroga.BudowaKosztorysu.dto;

import java.math.BigDecimal;

public record EstimateItemResponse(
        Long id,
        String name,
        String itemType,
        String unit,
        BigDecimal quantity,
        BigDecimal pricePerUnit,
        BigDecimal lineTotal,
        BigDecimal pricePerHour

) {
}
