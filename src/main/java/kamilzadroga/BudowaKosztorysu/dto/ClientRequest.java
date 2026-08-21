package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClientRequest(
        @NotBlank (message = "Pole nie może być puste")
        @Size(min = 1, max = 150, message = "Nazwa może mieć maksymalnie 150 znaków")
         String name,

        @NotBlank (message = "Pole nie może być puste")
        @Size(min = 1, max = 20, message = "Numer telefonu może mieć maksymalnie 20 znaków")
        String phoneNumber,

        @Email (message = "Nieprawidłowy format adresu email")
        @Size(max = 255, message = "Email może mieć maksymalnie 255 znaków")
        String email
) {
}
