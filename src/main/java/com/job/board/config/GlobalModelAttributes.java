package com.job.board.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {
    @ModelAttribute("requestURI")
    public String populateRequestUri(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
