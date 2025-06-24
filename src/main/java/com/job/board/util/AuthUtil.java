package com.job.board.util;

import com.job.board.entity.Job;
import com.job.board.entity.User;
import com.job.board.repository.JobApplicationRepository;
import com.job.board.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtil {
    private final UserService userService;
    private final JobApplicationRepository jobApplicationRepository;

    public AuthUtil(UserService userService, JobApplicationRepository jobApplicationRepository) {
        this.userService = userService;
        this.jobApplicationRepository = jobApplicationRepository;
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

    public void authorizeSeekerAppliedToJobForCompany(Long seekerId, Long jobId) {
        authorizeSeekerAppliedToJob(seekerId, jobId, "This seeker did not apply to this job");
    }

    public void authorizeSeekerAppliedToJobForSeeker(Long seekerId, Long jobId) {
        authorizeSeekerAppliedToJob(seekerId, jobId, "You did not apply to this job");
    }

    private void authorizeSeekerAppliedToJob(Long seekerId, Long jobId, String message) {
        boolean hasApplied = jobApplicationRepository.existsByJobIdAndJobSeekerId(jobId, seekerId);
        if (!hasApplied) {
            throw new AccessDeniedException(message);
        }
    }

    public void authorizeJobBelongsToCompany(Long jobId, Long companyId, Job job) {
        if (!job.getCompany().getId().equals(companyId)) {
            throw new AccessDeniedException("This job does not belong to the specified company");
        }
    }

}
