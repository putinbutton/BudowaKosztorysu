package kamilzadroga.BudowaKosztorysu.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class EstimateForm {

    @NotNull
    private Long projectId;

    @NotNull
    private LocalDate creationDate;
}
