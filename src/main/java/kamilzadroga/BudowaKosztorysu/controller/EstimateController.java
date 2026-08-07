package kamilzadroga.BudowaKosztorysu.controller;

import jakarta.validation.Valid;
import kamilzadroga.BudowaKosztorysu.dto.EstimateRequest;
import kamilzadroga.BudowaKosztorysu.dto.EstimateResponse;
import kamilzadroga.BudowaKosztorysu.service.EstimateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estimates")
@RequiredArgsConstructor
public class EstimateController {
    private final EstimateService service;

    @PostMapping
    public ResponseEntity<EstimateResponse> create (@Valid @RequestBody EstimateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public EstimateResponse getById (@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<EstimateResponse> getAll() {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById (@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
