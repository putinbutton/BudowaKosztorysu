package kamilzadroga.BudowaKosztorysu.web;

import jakarta.validation.Valid;
import kamilzadroga.BudowaKosztorysu.dto.ClientForm;
import kamilzadroga.BudowaKosztorysu.dto.ClientRequest;
import kamilzadroga.BudowaKosztorysu.dto.ClientResponse;
import kamilzadroga.BudowaKosztorysu.dto.ProjectResponse;
import kamilzadroga.BudowaKosztorysu.service.ClientService;
import kamilzadroga.BudowaKosztorysu.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientWebController {
    private final ClientService clientService;

    private final ProjectService projectService;

    @GetMapping
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.getAll());
        return "clients/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("client", new ClientForm());
        return "clients/form";
    }

    @PostMapping
    public String createClient(@Valid @ModelAttribute("client") ClientForm form, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return "clients/form";
        }
        ClientRequest request = new ClientRequest(form.getName(), form.getPhoneNumber(), form.getEmail());
        clientService.create(request);
        return "redirect:/clients";
    }

    @PostMapping("/{id}/delete")
    public String deleteClient(@PathVariable Long id) {
        clientService.delete(id);
        return "redirect:/clients";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        ClientResponse client = clientService.getById(id);
        ClientForm form = new ClientForm();
        form.setName(client.name());
        form.setPhoneNumber(client.phoneNumber());
        form.setEmail(client.email());

        model.addAttribute("client", form);
        model.addAttribute("clientId", id);

        return "clients/edit-form";
    }

    @PostMapping("/{id}/edit")
    public String updateClient(@PathVariable Long id, @Valid @ModelAttribute("client") ClientForm form, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()){
            model.addAttribute("clientId", id);
            return "clients/edit-form";
        }
        ClientRequest request = new ClientRequest(form.getName(), form.getPhoneNumber(), form.getEmail());
        clientService.update(id, request);

        return "redirect:/clients";
    }

    @GetMapping("/{id}/delete-confirm")
    public String showDeleteConfirmation (@PathVariable Long id, Model model) {
        ClientResponse client = clientService.getById(id);
        List<ProjectResponse> relatedProjects = projectService.getAll().stream()
                .filter(project -> project.clientId().equals(id))
                .toList();

        model.addAttribute("client", client);
        model.addAttribute("projects", relatedProjects);
        return "clients/delete-confirm";
    }
}
