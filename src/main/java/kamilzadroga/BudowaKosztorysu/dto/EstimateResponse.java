package kamilzadroga.BudowaKosztorysu.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record EstimateResponse(
        Long id,
        Long projectId,
        String projectName,
        LocalDate creationDate,
        List<EstimateItemResponse> items,
        BigDecimal totalAmount


) {
}
