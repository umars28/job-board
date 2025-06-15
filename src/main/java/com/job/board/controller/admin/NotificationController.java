package com.job.board.controller.admin;

import com.job.board.entity.Notification;
import com.job.board.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@RequestMapping("/notification")
@Controller
public class NotificationController {
    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public String listNotifications(Model model, Principal principal) {
        String username = principal.getName();
        List<Notification> notifications = notificationService.getAllNotifications(username);
        model.addAttribute("notifications", notifications);
        return "/admin/notification/index";
    }

    @PostMapping("/mark-all-read")
    public String markAllAsRead(Principal principal) {
        String username = principal.getName();
        notificationService.markAllAsRead(username);
        return "redirect:/notification";
    }

    @GetMapping("/redirect/{id}")
    public String redirectNotification(@PathVariable Long id) {
        Notification notification = notificationService.markAsRead(id);
        if (notification.getLink() != null && !notification.getLink().isEmpty()) {
            return "redirect:" + notification.getLink();
        } else {
            return "redirect:/notification";
        }
    }
}
