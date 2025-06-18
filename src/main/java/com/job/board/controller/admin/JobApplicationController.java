package com.job.board.controller.admin;

import com.job.board.entity.Job;
import com.job.board.entity.JobApplication;
import com.job.board.enums.ApplicantStatus;
import com.job.board.repository.JobApplicationRepository;
import com.job.board.service.JobApplicationService;
import com.job.board.service.JobService;
import com.job.board.util.AuthUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/jobs/applications")
@Controller
public class JobApplicationController {
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");

    private final JobService jobService;
    private final JobApplicationService jobApplicationService;
    private final JobApplicationRepository jobApplicationRepository;
    private final AuthUtil authUtil;

    public JobApplicationController(JobService jobService, JobApplicationService jobApplicationService, JobApplicationRepository jobApplicationRepository, AuthUtil authUtil) {
        this.jobService = jobService;
        this.jobApplicationService = jobApplicationService;
        this.jobApplicationRepository = jobApplicationRepository;
        this.authUtil = authUtil;
    }

    @GetMapping("/{id}/applicants")
    public String viewApplicant(@RequestParam(required = false) String status, @PathVariable("id") Long id, Model model) {
        Job job = jobService.jobDetails(id);
        authUtil.authorizeCompanyOrAdminAccessToJob(job);

        List<JobApplication> applications;

        if (status != null) {
            ApplicantStatus applicantStatus = ApplicantStatus.valueOf(status);
            applications = jobApplicationService.getApplicantByStatus(id, applicantStatus);
            model.addAttribute("selectedStatus", status);
        } else {
            applications = jobApplicationService.getApplicantsByJobId(id);
        }

        model.addAttribute("job", job);
        model.addAttribute("applications", applications);
        model.addAttribute("statuses", ApplicantStatus.values());
        return "/admin/job_applicants/index";
    }

    @PostMapping("/update-status")
    public String updateApplicationStatus(@RequestParam("applicationId") Long applicationId,
                                          @RequestParam("status") String status, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            ApplicantStatus newStatus = ApplicantStatus.valueOf(status);
            auditLogger.info("AUDIT - Request POST /jobs/applications/update-status application id={} to status={}", applicationId, newStatus);
            JobApplication application = jobApplicationService.updateApplicationStatus(applicationId, newStatus);
            Long jobId = application.getJob().getId();
            redirectAttributes.addFlashAttribute("message", "Status updated to " + newStatus.name());

            return "redirect:/jobs/applications/" + jobId + "/applicants?status=" + newStatus.name();

        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Invalid status value: " + status);
            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/");
        }
    }

}
