package kamilzadroga.BudowaKosztorysu.service;

import kamilzadroga.BudowaKosztorysu.dto.EstimateItemRequest;
import kamilzadroga.BudowaKosztorysu.dto.EstimateItemResponse;

public interface EstimateItemService {
    EstimateItemResponse addItem(Long estimateId, EstimateItemRequest request);
    EstimateItemResponse update(Long estimateId, Long id, EstimateItemRequest request);
    void delete(Long estimateId, Long itemId);
}
