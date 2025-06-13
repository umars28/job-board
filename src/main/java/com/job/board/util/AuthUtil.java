package com.job.board.util;

import com.job.board.entity.Job;
import com.job.board.entity.User;
import com.job.board.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    private final UserService userService;

    public AuthUtil(UserService userService) {
        this.userService = userService;
    }

    public Long getCurrentCompanyId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.getUserByUsername(username);
        if (user != null && user.getCompany() != null) {
            return user.getCompany().getId();
        }

        return null;
    }

    public void authorizeCompanyAccessToJob(Job job) {
        Long currentCompanyId = getCurrentCompanyId();
        if (!job.getCompany().getId().equals(currentCompanyId)) {
            throw new AccessDeniedException("Unauthorized access to this job");
        }
    }

    public void authorizeCompanyOrAdminAccessToJob(Job job) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return;
        }
        authorizeCompanyAccessToJob(job);
    }
}
