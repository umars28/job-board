package com.job.board.config;

import com.job.board.client.NotificationClient;
import com.job.board.entity.User;
import com.job.board.model.NotificationResponse;
import com.job.board.service.NotificationService;
import com.job.board.service.SeekerService;
import com.job.board.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalModelAttributes {
    private final UserService userService;
    private final NotificationClient notificationClient;
    private final SeekerService seekerService;

    public GlobalModelAttributes(UserService userService, NotificationService notificationService, NotificationClient notificationClient, SeekerService seekerService) {
        this.userService = userService;
        this.notificationClient = notificationClient;
        this.seekerService = seekerService;
    }

    @ModelAttribute("requestURI")
    public String populateRequestUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("currentJobSeekerId")
    public Long populateCurrentJobSeekerId() {
        return seekerService.getCurrentJobSeekerId();
    }

    @ModelAttribute
    public void addNotifications(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String username = auth.getName();

            List<NotificationResponse> notifications = notificationClient.getLatestNotifications(username);
            Long unreadCount = notificationClient.getUnreadCount(username);

            model.addAttribute("navbarNotifications", notifications);
            model.addAttribute("navbarUnreadCount", unreadCount);
        }
    }
}
