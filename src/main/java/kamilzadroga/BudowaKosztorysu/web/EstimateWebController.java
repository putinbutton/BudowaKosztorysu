package kamilzadroga.BudowaKosztorysu.web;

import jakarta.validation.Valid;
import kamilzadroga.BudowaKosztorysu.dto.EstimateForm;
import kamilzadroga.BudowaKosztorysu.dto.EstimateItemForm;
import kamilzadroga.BudowaKosztorysu.dto.EstimateRequest;
import kamilzadroga.BudowaKosztorysu.service.EstimateItemService;
import kamilzadroga.BudowaKosztorysu.service.EstimatePdfService;
import kamilzadroga.BudowaKosztorysu.service.EstimateService;
import kamilzadroga.BudowaKosztorysu.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/estimates")
@RequiredArgsConstructor
public class EstimateWebController {

    private final EstimateService estimateService;

    private final ProjectService projectService;

    private final EstimateItemService estimateItemService;

    private final EstimatePdfService estimatePdfService;

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("estimate", new EstimateForm());
        model.addAttribute("projects", projectService.getAll());

        return "estimates/form";
    }

    @GetMapping
    public String listEstimate(Model model) {
        model.addAttribute("estimates", estimateService.getAll());
        return "estimates/list";
    }

    @PostMapping
    public String createEstimate(@Valid @ModelAttribute("estimate") EstimateForm form) {
        EstimateRequest request = new EstimateRequest(form.getProjectId(), form.getCreationDate());
        estimateService.create(request);
        return "redirect:/estimates";
    }

    @GetMapping("/{id}")
    public String showEstimateDetails(@PathVariable Long id, Model model){

        model.addAttribute("estimate", estimateService.getById(id));
        model.addAttribute("itemForm", new EstimateItemForm());
        return "estimates/details";
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> downloadPdf(@PathVariable Long id, @RequestParam(defaultValue = "pl") String lang) {
        byte[] pdf = estimatePdfService.generatePdf(id, lang);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=kosztorys-" + id + " - " + lang + ".pdf")
                .body(pdf);
    }
}
