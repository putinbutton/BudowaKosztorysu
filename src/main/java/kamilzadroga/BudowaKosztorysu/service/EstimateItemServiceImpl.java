package kamilzadroga.BudowaKosztorysu.service;

import kamilzadroga.BudowaKosztorysu.dto.EstimateItemRequest;
import kamilzadroga.BudowaKosztorysu.dto.EstimateItemResponse;
import kamilzadroga.BudowaKosztorysu.exception.BudowaKosztorysuNotFoundException;
import kamilzadroga.BudowaKosztorysu.model.Estimate;
import kamilzadroga.BudowaKosztorysu.model.EstimateItem;
import kamilzadroga.BudowaKosztorysu.model.ItemType;
import kamilzadroga.BudowaKosztorysu.model.Unit;
import kamilzadroga.BudowaKosztorysu.repository.EstimateItemRepository;
import kamilzadroga.BudowaKosztorysu.repository.EstimateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
class EstimateItemServiceImpl implements EstimateItemService{

    private final EstimateItemRepository itemRepository;

    private final EstimateRepository estimateRepository;

    private final CurrentUserService currentUserService;

    @Override
    public EstimateItemResponse addItem(Long estimateId, EstimateItemRequest request) {
        Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(estimateId));


        EstimateItem estimateItem = new EstimateItem();
        estimateItem.setEstimate(estimate);

        if(!estimateItem.getEstimate().getProject().getClient().getOwner().equals(currentUserService.getCurrentUser())) {
            throw new BudowaKosztorysuNotFoundException(estimateId);
        }

               estimateItem.setName(request.name());
               estimateItem.setItemType(ItemType.valueOf(request.itemType()));
               estimateItem.setUnit(Unit.valueOf(request.unit()));
               estimateItem.setQuantity(request.quantity());
               estimateItem.setPricePerUnit(request.pricePerUnit());
               estimateItem.setPricePerHour(request.pricePerHour());

               EstimateItem saved = itemRepository.save(estimateItem);

               return mapToResponse(saved);

    }

    @Override
    public EstimateItemResponse update(Long estimateId, Long id, EstimateItemRequest request) {
        EstimateItem item = itemRepository.findById(id)
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(id));
        if(!item.getEstimate().getId().equals(estimateId)){
            throw  new BudowaKosztorysuNotFoundException(id);
        }

        if(!item.getEstimate().getProject().getClient().getOwner().equals(currentUserService.getCurrentUser())) {
            throw new BudowaKosztorysuNotFoundException(estimateId);
        }

        item.setName(request.name());
        item.setItemType(ItemType.valueOf(request.itemType()));
        item.setUnit(Unit.valueOf(request.unit()));
        item.setQuantity(request.quantity());
        item.setPricePerUnit(request.pricePerUnit());
        item.setPricePerHour(request.pricePerHour());

        EstimateItem saved = itemRepository.save(item);

        return mapToResponse(saved);
    }

    @Override
    public void delete(Long estimateId, Long itemId) {
        EstimateItem item = itemRepository.findById(itemId)
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(itemId));

        if(!item.getEstimate().getId().equals(estimateId)) {
            throw new BudowaKosztorysuNotFoundException(itemId);
        }

        if(!item.getEstimate().getProject().getClient().getOwner().equals(currentUserService.getCurrentUser())) {
            throw new BudowaKosztorysuNotFoundException(itemId);
        }
        itemRepository.deleteById(itemId);
    }

    private EstimateItemResponse mapToResponse(EstimateItem item) {
        return new EstimateItemResponse(
                item.getId(),
                item.getName(),
                item.getItemType().name(),
                item.getUnit().name(),
                item.getQuantity(),
                item.getPricePerUnit(),
                item.getQuantity().multiply(item.getPricePerUnit()),
                item.getPricePerHour()
        );

    }
}
