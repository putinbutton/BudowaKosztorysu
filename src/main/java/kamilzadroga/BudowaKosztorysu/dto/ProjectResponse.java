package kamilzadroga.BudowaKosztorysu.dto;

import java.time.LocalDate;

public record ProjectResponse(
        Long id,
        Long clientId,
        String clientName,
        String name,
        LocalDate creationDate
) {
}
