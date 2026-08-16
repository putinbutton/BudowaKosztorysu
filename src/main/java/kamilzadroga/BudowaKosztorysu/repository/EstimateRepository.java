package kamilzadroga.BudowaKosztorysu.repository;


import kamilzadroga.BudowaKosztorysu.model.Estimate;
import kamilzadroga.BudowaKosztorysu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstimateRepository extends JpaRepository<Estimate, Long> {
    List<Estimate> findByProject_Client_Owner(User owner);
}
