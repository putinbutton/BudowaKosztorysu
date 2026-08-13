package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String companyName,
        String address,
        String phone,
        @Email String email,
        String nip
) {
}
