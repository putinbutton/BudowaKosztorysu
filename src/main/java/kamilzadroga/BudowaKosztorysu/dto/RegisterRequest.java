package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank(message = "Pole nie może być puste") String username,
        @NotBlank(message = "Pole nie może być puste") String password,
        @NotBlank(message = "Pole nie może być puste") String companyName,
        String address,
        String phone,
        @Email String email,
        String nip
) {
}
