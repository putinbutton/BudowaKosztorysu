package kamilzadroga.BudowaKosztorysu.dto;

public record ClientResponse(
        Long id,
        String name,
        String phoneNumber,
        String email
) {
}
