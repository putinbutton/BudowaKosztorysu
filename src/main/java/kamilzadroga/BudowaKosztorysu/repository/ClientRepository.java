package kamilzadroga.BudowaKosztorysu.repository;

import kamilzadroga.BudowaKosztorysu.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {
}
