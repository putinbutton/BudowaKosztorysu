package kamilzadroga.BudowaKosztorysu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@Table(name ="users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(name = "failed_login_attempts")
    private int failedLoginAttempts = 0;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @Column(unique = true)
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
