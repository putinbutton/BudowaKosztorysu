package kamilzadroga.BudowaKosztorysu.service;

import kamilzadroga.BudowaKosztorysu.dto.EstimateItemResponse;
import kamilzadroga.BudowaKosztorysu.dto.EstimateRequest;
import kamilzadroga.BudowaKosztorysu.dto.EstimateResponse;
import kamilzadroga.BudowaKosztorysu.exception.BudowaKosztorysuNotFoundException;
import kamilzadroga.BudowaKosztorysu.model.Estimate;
import kamilzadroga.BudowaKosztorysu.model.EstimateItem;
import kamilzadroga.BudowaKosztorysu.model.Project;
import kamilzadroga.BudowaKosztorysu.model.User;
import kamilzadroga.BudowaKosztorysu.repository.EstimateRepository;
import kamilzadroga.BudowaKosztorysu.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;



import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
class EstimateServiceImpl implements EstimateService{

    private final EstimateRepository repository;

    private final ProjectRepository projectRepository;

    private final CurrentUserService currentUserService;

    @Override
    public EstimateResponse create(EstimateRequest request) {
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(request.projectId()));

        if(!project.getClient().getOwner().equals(currentUserService.getCurrentUser())) {
            throw new BudowaKosztorysuNotFoundException(request.projectId());
        }

        Estimate estimate = new Estimate();
        estimate.setCreationDate(request.creationDate());
        estimate.setProject(project);

        Estimate saved = repository.save(estimate);

        return mapToResponse(saved);
    }

    @Override
    public EstimateResponse getById(Long id) {
        Estimate estimate = repository.findById(id)
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(id));

        if(!estimate.getProject().getClient().getOwner().equals(currentUserService.getCurrentUser())) {
            throw new BudowaKosztorysuNotFoundException(id);
        }
        return mapToResponse(estimate);

    }

    @Override
    public List<EstimateResponse> getAll() {
        User currentUser = currentUserService.getCurrentUser();
        return repository.findByProject_Client_Owner(currentUser).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {

        Estimate estimate = repository.findById(id)
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(id));

        if(!estimate.getProject().getClient().getOwner().equals(currentUserService.getCurrentUser())) {
            throw new BudowaKosztorysuNotFoundException(id);
        }
        repository.deleteById(id);
    }

    private BigDecimal calculateTotal(List<EstimateItem> items) {
        if(items == null || items.isEmpty()){
            return BigDecimal.ZERO;
        }

        return items.stream()
                .map(item -> {
                    BigDecimal quantity = item.getQuantity() != null ? item.getQuantity() : BigDecimal.ZERO;
                    BigDecimal price = item.getPricePerUnit() != null ? item.getPricePerUnit() : BigDecimal.ZERO;
                    return quantity.multiply(price);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    private EstimateResponse mapToResponse(Estimate estimate) {
        List<EstimateItemResponse> itemResponses = estimate.getItems().stream()
                .map(item -> new EstimateItemResponse(
                        item.getId(),
                        item.getName(),
                        item.getItemType().name(),
                        item.getUnit().name(),
                        item.getQuantity(),
                        item.getPricePerUnit(),
                        item.getQuantity().multiply(item.getPricePerUnit()),
                        item.getPricePerHour()
                ))
                .toList();
        return new EstimateResponse(
                estimate.getId(),
                estimate.getProject().getId(),
                estimate.getProject().getName(),
                estimate.getCreationDate(),
                itemResponses,
                calculateTotal(estimate.getItems())
        );
    }
}
