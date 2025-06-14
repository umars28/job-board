package com.job.board.controller.seeker;

import com.job.board.entity.JobSeeker;
import com.job.board.entity.User;
import com.job.board.service.SeekerService;
import com.job.board.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@RequestMapping("/seeker")
@Controller("publicSeekerController")
public class SeekerController {
    private final UserService userService;
    private final SeekerService seekerService;

    public SeekerController(UserService userService, SeekerService seekerService) {
        this.userService = userService;
        this.seekerService = seekerService;
    }

    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        String username = principal.getName();
        User user = userService.getUserByUsername(username);
        model.addAttribute("user", user);

        return "/public/seeker/profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(
            @ModelAttribute("user") User formUser,
            Principal principal,
            RedirectAttributes redirectAttributes
    ) {
        userService.updateProfile(formUser, principal.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Profil berhasil diperbarui.");

        return "redirect:/seeker/profile?success";
    }

        @GetMapping("/job/applied")
    public String appliedJob() {
        return "/public/seeker/applied-job";
    }

    @GetMapping("/notification")
    public String notification() {
        return "/public/seeker/notification";
    }

    @GetMapping("/chat")
    public String chat() {
        return "/public/seeker/chat";
    }
}
