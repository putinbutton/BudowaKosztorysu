package kamilzadroga.BudowaKosztorysu.service;

import kamilzadroga.BudowaKosztorysu.dto.ProjectRequest;
import kamilzadroga.BudowaKosztorysu.dto.ProjectResponse;
import kamilzadroga.BudowaKosztorysu.exception.BudowaKosztorysuNotFoundException;
import kamilzadroga.BudowaKosztorysu.model.Client;
import kamilzadroga.BudowaKosztorysu.model.Project;
import kamilzadroga.BudowaKosztorysu.model.User;
import kamilzadroga.BudowaKosztorysu.repository.ClientRepository;
import kamilzadroga.BudowaKosztorysu.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository repository;

    private final ClientRepository clientRepository;

    private final CurrentUserService currentUserService;
    @Override
    public ProjectResponse create(ProjectRequest request) {
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(request.clientId()));

        if (!client.getOwner().equals(currentUserService.getCurrentUser())) {
            throw new BudowaKosztorysuNotFoundException(request.clientId());
        }

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
        if(!project.getClient().getOwner().equals(currentUserService.getCurrentUser())) {
            throw new BudowaKosztorysuNotFoundException(id);
        }
        return new ProjectResponse(
                project.getId(),
                project.getClient().getId(),
                project.getClient().getName(),
                project.getName(),
                project.getCreationDate());
    }

    @Override
    public List<ProjectResponse> getAll() {
        User currentUser = currentUserService.getCurrentUser();
        return repository.findByClient_Owner(currentUser).stream()
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

        if(!project.getClient().getOwner().equals(currentUserService.getCurrentUser())) {
            throw new BudowaKosztorysuNotFoundException(id);
        }

        if(!client.getOwner().equals(currentUserService.getCurrentUser())){
            throw new BudowaKosztorysuNotFoundException(request.clientId());
        }

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

        Project project = repository.findById(id)
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(id));
        if(!project.getClient().getOwner().equals(currentUserService.getCurrentUser())) {
            throw new BudowaKosztorysuNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
