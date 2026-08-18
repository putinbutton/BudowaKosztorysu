package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class RegisterForm {
    @NotBlank(message = "Pole nie może być puste")
    private String username;

    @NotBlank(message = "Pole nie może być puste")
    private String password;

    @NotBlank(message = "Pole nie może być puste")
    private String companyName;

    private String address;

    private String phone;

    @Email
    private String email;

    private String nip;
}
