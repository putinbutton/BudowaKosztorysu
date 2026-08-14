package kamilzadroga.BudowaKosztorysu.web;

import jakarta.validation.Valid;
import kamilzadroga.BudowaKosztorysu.dto.RegisterForm;
import kamilzadroga.BudowaKosztorysu.dto.RegisterRequest;
import kamilzadroga.BudowaKosztorysu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UserWebController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register (@Valid @ModelAttribute("registerForm") RegisterForm form) {
        RegisterRequest request = new RegisterRequest(
                form.getUsername(),
                form.getPassword(),
                form.getCompanyName(),
                form.getAddress(),
                form.getPhone(),
                form.getEmail(),
                form.getNip()
        );

        userService.register(request);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }
}
