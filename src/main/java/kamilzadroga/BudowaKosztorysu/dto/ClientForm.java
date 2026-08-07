package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientForm {

    @NotBlank
    private String name;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    @Email
    private String email;
}
