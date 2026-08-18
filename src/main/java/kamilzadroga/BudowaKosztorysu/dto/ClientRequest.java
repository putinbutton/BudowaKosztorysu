package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ClientRequest(
        @NotBlank (message = "Pole nie może być puste")
         String name,

        @NotBlank (message = "Pole nie może być puste")
         String phoneNumber,

        @Email (message = "Nieprawidłowy format adresu email")
         String email
) {
}
