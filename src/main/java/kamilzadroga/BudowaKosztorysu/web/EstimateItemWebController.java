package kamilzadroga.BudowaKosztorysu.web;

import jakarta.validation.Valid;
import kamilzadroga.BudowaKosztorysu.dto.*;
import kamilzadroga.BudowaKosztorysu.exception.BudowaKosztorysuNotFoundException;
import kamilzadroga.BudowaKosztorysu.service.EstimateItemService;
import kamilzadroga.BudowaKosztorysu.service.EstimateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/estimates/{estimateId}/items")
public class EstimateItemWebController {

    private final EstimateItemService estimateItemService;

    private final EstimateService estimateService;

    @PostMapping
    public String addItem(@PathVariable Long estimateId, @Valid @ModelAttribute("itemForm")EstimateItemForm form, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("estimate", estimateService.getById(estimateId));
            return "estimates/details";
        }
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

    @PostMapping("/{itemId}/delete")
    public String deleteItem(@PathVariable Long estimateId, @PathVariable Long itemId) {
        estimateItemService.delete(estimateId, itemId);
        return "redirect:/estimates/" + estimateId;
    }

    @GetMapping("/{itemId}/edit")
    public String showEditForm(@PathVariable Long estimateId, Model model, @PathVariable Long itemId) {

        EstimateResponse estimate = estimateService.getById(estimateId);

        EstimateItemResponse item = estimate.items().stream()
                .filter(i -> i.id().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new BudowaKosztorysuNotFoundException(itemId));

        EstimateItemForm form = new EstimateItemForm();
        form.setName(item.name());
        form.setItemType(item.itemType());
        form.setUnit(item.unit());
        form.setQuantity(item.quantity());
        form.setPricePerUnit(item.pricePerUnit());
        form.setPricePerHour(item.pricePerHour());

        model.addAttribute("itemForm", form);
        model.addAttribute("estimateId", estimateId);
        model.addAttribute("itemId", itemId);

        return "estimates/item-edit-form";
    }

    @PostMapping("/{itemId}/edit")
    public String updateEstimateItem(@PathVariable Long estimateId,@PathVariable Long itemId, @Valid @ModelAttribute("itemForm") EstimateItemForm form, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("estimateId", estimateId);
            model.addAttribute("itemId", itemId);
            return "estimates/item-edit-form";
        }
        EstimateItemRequest request = new EstimateItemRequest(
                form.getName(),
                form.getItemType(),
                form.getUnit(),
                form.getQuantity(),
                form.getPricePerUnit(),
                form.getPricePerHour());
        estimateItemService.update(estimateId, itemId, request);

        return "redirect:/estimates/" + estimateId;
    }

    }



