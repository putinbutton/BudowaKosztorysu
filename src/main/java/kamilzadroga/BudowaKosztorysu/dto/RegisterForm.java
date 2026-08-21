package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class RegisterForm {
    @NotBlank(message = "Pole nie może być puste")
    @Size(min = 3, max = 50, message = "Login może mieć od 3 do 50 znaków")
    private String username;

    @NotBlank(message = "Pole nie może być puste")
    @Size(min = 8, max = 72, message = "Hasło musi mieć od 8 do 72 znaków")
    private String password;

    @NotBlank(message = "Pole nie może być puste")
    @Size(min = 1, max = 150, message = "Nazwa firmy może mieć maksymalnie 150 znaków")
    private String companyName;

    @Size(max = 255, message = "Adres może mieć maksymalnie 255 znaków")
    private String address;

    @Size(max = 20, message = "Numer telefonu może mieć maksymalnie 20 znaków")
    private String phone;

    @Email
    @Size(max = 255, message = "Email może mieć maksymalnie 255 znaków")
    private String email;

    @Size(max = 20, message = "NIP może mieć maksymalnie 20 znaków")
    private String nip;
}
