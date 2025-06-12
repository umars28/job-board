package com.job.board.controller;

import com.job.board.service.CompanyService;
import com.job.board.service.JobApplicationService;
import com.job.board.service.JobService;
import com.job.board.service.SeekerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashbordController {
    private final CompanyService companyService;
    private final SeekerService seekerService;
    private final JobService jobService;
    private final JobApplicationService jobApplicationService;

    public DashbordController(CompanyService companyService, SeekerService seekerService, JobService jobService, JobApplicationService jobApplicationService) {
        this.companyService = companyService;
        this.seekerService = seekerService;
        this.jobService = jobService;
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("totalCompanies", companyService.getTotalCompanies());
        model.addAttribute("totalSeekers", seekerService.getTotalJobSeekers());
        model.addAttribute("totalActiveJobs", jobService.getTotalActiveJobPostings());
        model.addAttribute("totalApplications", jobApplicationService.getTotalApplications());
        return "admin/dashboard/index";
    }
}
