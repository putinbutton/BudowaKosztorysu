package kamilzadroga.BudowaKosztorysu.service;

import kamilzadroga.BudowaKosztorysu.dto.ProjectRequest;
import kamilzadroga.BudowaKosztorysu.dto.ProjectResponse;
import kamilzadroga.BudowaKosztorysu.exception.BudowaKosztorysuNotFoundException;
import kamilzadroga.BudowaKosztorysu.model.Client;
import kamilzadroga.BudowaKosztorysu.model.Project;
import kamilzadroga.BudowaKosztorysu.repository.ClientRepository;
import kamilzadroga.BudowaKosztorysu.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository repository;

    private final ClientRepository clientRepository;
    @Override
    public ProjectResponse create(ProjectRequest request) {
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(request.clientId()));

        Project project = new Project();

        project.setClient(client);
        project.setName(request.name());
        project.setCreationDate(request.creationDate());

        Project saved = repository.save(project);
        return new ProjectResponse(
                saved.getId(),
                saved.getClient().getId(),
                saved.getClient().getName(),
                saved.getName(),
                saved.getCreationDate()
        );
    }

    @Override
    public ProjectResponse getById(Long id) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(id));
        return new ProjectResponse(
                project.getId(),
                project.getClient().getId(),
                project.getClient().getName(),
                project.getName(),
                project.getCreationDate());
    }

    @Override
    public List<ProjectResponse> getAll() {
        return repository.findAll().stream()
                .map(project -> new ProjectResponse(
                        project.getId(),
                        project.getClient().getId(),
                        project.getClient().getName(),
                        project.getName(),
                        project.getCreationDate()
                ))
                .toList();
    }

    @Override
    public ProjectResponse update(Long id, ProjectRequest request) {
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(request.clientId()));
        Project project = repository.findById(id)
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(id));


        project.setClient(client);
        project.setName(request.name());
        project.setCreationDate(request.creationDate());

        Project updated = repository.save(project);

        return new ProjectResponse(
                updated.getId(),
                updated.getClient().getId(),
                updated.getClient().getName(),
                updated.getName(),
                updated.getCreationDate()
        );
    }

    @Override
    public void delete(Long id) {
        if(!repository.existsById(id)) {
            throw new BudowaKosztorysuNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
