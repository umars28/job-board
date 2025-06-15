package com.job.board.controller.seeker;

import com.job.board.entity.JobApplication;
import com.job.board.entity.JobSeeker;
import com.job.board.entity.Notification;
import com.job.board.entity.User;
import com.job.board.service.JobApplicationService;
import com.job.board.service.NotificationService;
import com.job.board.service.SeekerService;
import com.job.board.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@RequestMapping("/seeker")
@Controller
public class SeekerController {
    private final UserService userService;
    private final SeekerService seekerService;
    private final JobApplicationService jobApplicationService;
    private final NotificationService notificationService;

    public SeekerController(UserService userService, SeekerService seekerService, JobApplicationService jobApplicationService, NotificationService notificationService) {
        this.userService = userService;
        this.seekerService = seekerService;
        this.jobApplicationService = jobApplicationService;
        this.notificationService = notificationService;
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
    public String appliedJob(Model model, Principal principal) {
        String username = principal.getName();
        List<JobApplication> applications = jobApplicationService.getJobApplicationsByUsername(username);
        model.addAttribute("applications", applications);
        return "/public/seeker/applied-job";
    }

    @GetMapping("/notification")
    public String listNotifications(Model model, Principal principal) {
        String username = principal.getName();
        List<Notification> notifications = notificationService.getAllNotifications(username);
        model.addAttribute("notifications", notifications);
        return "/public/seeker/notification";
    }

    @GetMapping("/notification/redirect/{id}")
    public String redirectNotification(@PathVariable Long id) {
        Notification notification = notificationService.markAsRead(id);
        if (notification.getLink() != null && !notification.getLink().isEmpty()) {
            return "redirect:" + notification.getLink();
        } else {
            return "redirect:/seeker/notification";
        }
    }

    @PostMapping("/notification/mark-all-read")
    public String markAllAsRead(Principal principal) {
        String username = principal.getName();
        notificationService.markAllAsRead(username);
        return "redirect:/seeker/notification";
    }

    @GetMapping("/chat")
    public String chat() {
        return "/public/seeker/chat";
    }
}
