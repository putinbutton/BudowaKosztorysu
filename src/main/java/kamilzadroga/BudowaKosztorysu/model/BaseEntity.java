package kamilzadroga.BudowaKosztorysu.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "version_id")
    private Integer version = 0;

    @Column(name = "uuid", updatable = false)
    private UUID uuid = UUID.randomUUID();

    @Override
    public boolean equals(Object that) {
        return this == that
                || (that instanceof BaseEntity && Objects.equals(uuid,((BaseEntity) that).uuid));
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
