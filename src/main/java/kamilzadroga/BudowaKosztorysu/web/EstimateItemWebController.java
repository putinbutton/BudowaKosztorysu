package kamilzadroga.BudowaKosztorysu.web;

import jakarta.validation.Valid;
import kamilzadroga.BudowaKosztorysu.dto.EstimateItemForm;
import kamilzadroga.BudowaKosztorysu.dto.EstimateItemRequest;
import kamilzadroga.BudowaKosztorysu.service.EstimateItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/estimates/{estimateId}/items")
public class EstimateItemWebController {

    private final EstimateItemService estimateItemService;

    @PostMapping
    public String addItem(@PathVariable Long estimateId, @Valid @ModelAttribute("itemForm")EstimateItemForm form) {
        EstimateItemRequest request = new EstimateItemRequest(
                form.getName(),
                form.getItemType(),
                form.getUnit(),
                form.getQuantity(),
                form.getPricePerUnit(),
                form.getPricePerHour());
        estimateItemService.addItem(estimateId, request);
        return "redirect:/estimates/" + estimateId;
    }

}
