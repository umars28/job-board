package com.job.board.controller.admin;

import com.job.board.entity.User;
import com.job.board.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@RequestMapping("/profile")
@Controller
public class ProfileController {
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String editProfile(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);
        return "/admin/profile/index";
    }

    @PostMapping("/update")
    public String updateProfile(@ModelAttribute("user") User formUser, Principal principal, RedirectAttributes redirectAttributes) {
        String username = principal.getName();
        auditLogger.info("AUDIT - Request POST /profile/update for username={}", username);
        userService.updateProfile(formUser, username);
        redirectAttributes.addFlashAttribute("successMessage", "Profil berhasil diperbarui.");
        return "redirect:/profile?success";
    }
}
