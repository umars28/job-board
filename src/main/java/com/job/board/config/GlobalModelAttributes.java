package com.job.board.config;

import com.job.board.client.NotificationClient;
import com.job.board.entity.Notification;
import com.job.board.entity.User;
import com.job.board.model.NotificationResponse;
import com.job.board.service.NotificationService;
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
    private final NotificationService notificationService;
    private final NotificationClient notificationClient;

    public GlobalModelAttributes(UserService userService, NotificationService notificationService, NotificationClient notificationClient) {
        this.userService = userService;
        this.notificationService = notificationService;
        this.notificationClient = notificationClient;
    }

    @ModelAttribute("requestURI")
    public String populateRequestUri(HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("currentJobSeekerId")
    public Long populateCurrentJobSeekerId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            String username = auth.getName();
            User user = userService.getUserByUsername(username);
            if (user != null && user.getJobSeeker() != null) {
                return user.getJobSeeker().getId();
            }
        }
        return null;
    }

    @ModelAttribute
    public void addNotifications(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !auth.getPrincipal().equals("anonymousUser")) {
            String username = auth.getName();
//            List<Notification> notifications = notificationService.getLatestNotifications(username);
//            long unreadCount = notificationService.countUnread(username);

            List<NotificationResponse> notifications = notificationClient.getLatestNotifications(username);
            Long unreadCount = notificationClient.getUnreadCount(username);

            model.addAttribute("navbarNotifications", notifications);
            model.addAttribute("navbarUnreadCount", unreadCount);
        }
    }
}
