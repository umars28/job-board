package com.job.board.controller;

import com.job.board.entity.Job;
import com.job.board.entity.JobApplication;
import com.job.board.enums.ApplicantStatus;
import com.job.board.service.JobApplicationService;
import com.job.board.service.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/jobs/applications")
@Controller
public class JobApplicationController {

    private final JobService jobService;
    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobService jobService, JobApplicationService jobApplicationService) {
        this.jobService = jobService;
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping("/{id}/applicants")
    public String viewApplicant(@RequestParam(required = false) String status, @PathVariable("id") Long id, Model model) {
        Job job = jobService.jobDetails(id);

        List<JobApplication> applications;

        if (status != null) {
            ApplicantStatus selectedStatus = ApplicantStatus.valueOf(status);
            model.addAttribute("selectedStatus", selectedStatus);
            applications = jobApplicationService.getApplicantByStatus(id, selectedStatus);

        } else {
            applications = jobApplicationService.getApplicantsByJobId(id);
        }

        model.addAttribute("job", job);
        model.addAttribute("applications", applications);
        model.addAttribute("statuses", ApplicantStatus.values());
        return "/admin/job_applicants/index";
    }
}
