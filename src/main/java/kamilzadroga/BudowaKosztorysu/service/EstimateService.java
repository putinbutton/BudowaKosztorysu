package kamilzadroga.BudowaKosztorysu.service;

import kamilzadroga.BudowaKosztorysu.dto.EstimateRequest;
import kamilzadroga.BudowaKosztorysu.dto.EstimateResponse;

import java.util.List;

public interface EstimateService {
    EstimateResponse create (EstimateRequest request);
    EstimateResponse getById(Long id);
    List<EstimateResponse> getAll();
    void delete(Long id);
}
