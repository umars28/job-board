package com.job.board.config;

import com.job.board.entity.User;
import com.job.board.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    private final UserService userService;

    public GlobalModelAttributes(UserService userService) {
        this.userService = userService;
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
}
