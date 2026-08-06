package kamilzadroga.BudowaKosztorysu.controller;


import jakarta.validation.Valid;
import kamilzadroga.BudowaKosztorysu.dto.ClientRequest;
import kamilzadroga.BudowaKosztorysu.dto.ClientResponse;

import kamilzadroga.BudowaKosztorysu.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService service;

    @PostMapping
    public ResponseEntity<ClientResponse> create (@RequestBody @Valid ClientRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ClientResponse getById (@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public List<ClientResponse> getAll () {
        return service.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById (@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ClientResponse updateById(@Valid @RequestBody ClientRequest request, @PathVariable Long id) {
        return service.update(id, request);
    }
}
