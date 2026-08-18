package kamilzadroga.BudowaKosztorysu.web;

import jakarta.validation.Valid;
import kamilzadroga.BudowaKosztorysu.dto.ProjectForm;
import kamilzadroga.BudowaKosztorysu.dto.ProjectRequest;
import kamilzadroga.BudowaKosztorysu.service.ClientService;
import kamilzadroga.BudowaKosztorysu.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectWebController {
    private final ProjectService projectService;

    private final ClientService clientService;

    @GetMapping("/new")
    public String showCreateForm(Model model){
        model.addAttribute("project", new ProjectForm());
        model.addAttribute("clients", clientService.getAll());

        return "projects/form";
    }

    @GetMapping
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.getAll());
        return "projects/list";
    }

    @PostMapping
    public String createProject(@Valid @ModelAttribute("project") ProjectForm form, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("clients", clientService.getAll());
            return "projects/form";
        }
        ProjectRequest request = new ProjectRequest(form.getClientId(), form.getName(), form.getCreationDate());
        projectService.create(request);
        return "redirect:/projects";
    }
}
