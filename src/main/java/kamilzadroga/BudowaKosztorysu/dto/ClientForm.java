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

    @NotBlank (message = "Pole nie może być puste")
    private String name;

    @NotBlank (message = "Pole nie może być puste")
    private String phoneNumber;

    @Email (message = "Nieprawidłowy format adresu email")
    private String email;
}
