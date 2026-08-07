package kamilzadroga.BudowaKosztorysu.controller;

import jakarta.validation.Valid;
import kamilzadroga.BudowaKosztorysu.dto.EstimateItemRequest;
import kamilzadroga.BudowaKosztorysu.dto.EstimateItemResponse;
import kamilzadroga.BudowaKosztorysu.service.EstimateItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estimates/{estimateId}/items")
@RequiredArgsConstructor
public class EstimateItemController {
    private final EstimateItemService service;

    @PostMapping
    public ResponseEntity<EstimateItemResponse> create (@PathVariable Long estimateId, @Valid @RequestBody EstimateItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addItem(estimateId, request));
    }

    @PutMapping("/{itemId}")
    public EstimateItemResponse update(@PathVariable Long estimateId, @PathVariable Long itemId,@Valid @RequestBody EstimateItemRequest request) {
        return service.update(estimateId, itemId, request);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteById (@PathVariable Long estimateId, @PathVariable Long itemId) {
        service.delete(estimateId, itemId);
        return ResponseEntity.noContent().build();
    }
}
