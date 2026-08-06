package kamilzadroga.BudowaKosztorysu.service;

import kamilzadroga.BudowaKosztorysu.dto.ProjectRequest;
import kamilzadroga.BudowaKosztorysu.dto.ProjectResponse;

import java.util.List;

public interface ProjectService {
    ProjectResponse create(ProjectRequest request);
    ProjectResponse getById(Long id);
    List<ProjectResponse> getAll();
    ProjectResponse update(Long id, ProjectRequest request);
    void delete(Long id);
}
