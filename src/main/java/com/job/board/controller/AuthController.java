package com.job.board.controller;

import com.job.board.enums.Role;
import com.job.board.model.LoginRequest;
import com.job.board.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
                && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            return "redirect:/";
        }
        model.addAttribute("loginRequest", new LoginRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String doLogin(
            @Valid @ModelAttribute("loginRequest") LoginRequest loginRequest,
            BindingResult bindingResult,
            Model model,
            HttpSession session
    ) {

        if(bindingResult.hasErrors()) {
            return "auth/login";
        }

        boolean success = authService.login(loginRequest, session);

        if (success) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            boolean isJobSeeker = authService.isJobSeeker(auth);
            if (isJobSeeker) {
                return "redirect:/";
            }
            return "redirect:/dashboard";
        }

        model.addAttribute("error", "Username atau password salah");
        return "auth/login";
    }

    @PostMapping("/logout")
    public String logout(HttpSession httpSession) {
        authService.logout(httpSession);
        return "redirect:/";
    }
}
