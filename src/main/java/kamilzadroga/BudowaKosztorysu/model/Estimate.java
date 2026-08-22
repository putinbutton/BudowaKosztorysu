package kamilzadroga.BudowaKosztorysu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Audited
@Getter
@Setter
@NoArgsConstructor

public class Estimate extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    private Project project;

    @NotNull
    private LocalDate creationDate;

    @OneToMany(mappedBy = "estimate")
    private List<EstimateItem> items = new ArrayList<>();

}
