package com.job.board.controller.admin;

import com.job.board.client.NotificationClient;
import com.job.board.entity.Notification;
import com.job.board.model.NotificationResponse;
import com.job.board.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private final NotificationService notificationService;
    private final NotificationClient notificationClient;

    public NotificationController(NotificationService notificationService, NotificationClient notificationClient) {
        this.notificationService = notificationService;
        this.notificationClient = notificationClient;
    }

    @GetMapping
    public String listNotifications(Model model, Principal principal) {
        String username = principal.getName();
        List<NotificationResponse> notifications = notificationService.getAllNotifications(username);
        model.addAttribute("notifications", notifications);
        return "/admin/notification/index";
    }

    @PostMapping("/mark-all-read")
    public String markAllAsRead(Principal principal) {
        String username = principal.getName();
        auditLogger.info("AUDIT - Request POST /notification/mark-all-read for username={}", username);
        notificationClient.markAllAsRead(username);
        return "redirect:/notification";
    }

    @GetMapping("/redirect/{id}")
    public String redirectNotification(@PathVariable Long id) {
        auditLogger.info("AUDIT - Request GET /notification/redirect/{}", id);
        NotificationResponse notification = notificationClient.markAsRead(id);
        if (notification.getLink() != null && !notification.getLink().isEmpty()) {
            return "redirect:" + notification.getLink();
        } else {
            return "redirect:/notification";
        }
    }
}
