package kamilzadroga.BudowaKosztorysu.controller;

import jakarta.validation.Valid;
import kamilzadroga.BudowaKosztorysu.dto.ProjectRequest;
import kamilzadroga.BudowaKosztorysu.dto.ProjectResponse;
import kamilzadroga.BudowaKosztorysu.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping("/{id}")
    public ProjectResponse getById(@PathVariable Long id){
        return service.getById(id);
    }

    @GetMapping
    public List<ProjectResponse> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public ProjectResponse updateById(@PathVariable Long id, @Valid @RequestBody ProjectRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
