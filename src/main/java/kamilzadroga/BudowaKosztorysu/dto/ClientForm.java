package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientForm {

    @NotBlank (message = "Pole nie może być puste")
    @Size(min = 1, max = 150, message = "Nazwa może mieć maksymalnie 150 znaków")
    private String name;

    @NotBlank (message = "Pole nie może być puste")
    @Size(min = 1, max = 20, message = "Numer telefonu może mieć maksymalnie 20 znaków")
    private String phoneNumber;

    @Email (message = "Nieprawidłowy format adresu email")
    @Size(max = 255, message = "Email może mieć maksymalnie 255 znaków")
    private String email;
}
