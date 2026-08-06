package kamilzadroga.BudowaKosztorysu.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EstimateItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Estimate estimate;

    @NotBlank
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Unit unit;

    @NotNull
    private BigDecimal quantity;

    @NotNull
    private BigDecimal pricePerUnit;

    private BigDecimal pricePerHour;

}
