package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class RegisterForm {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String companyName;

    private String address;

    private String phone;

    @Email
    private String email;

    private String nip;
}
