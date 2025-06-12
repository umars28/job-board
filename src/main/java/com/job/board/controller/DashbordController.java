package com.job.board.controller;

import com.job.board.entity.Company;
import com.job.board.service.CompanyService;
import com.job.board.service.JobApplicationService;
import com.job.board.service.JobService;
import com.job.board.service.SeekerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("totalCompanies", companyService.getTotalCompanies());
            model.addAttribute("totalSeekers", seekerService.getTotalJobSeekers());
            model.addAttribute("totalActiveJobs", jobService.getTotalActiveJobPostings());
            model.addAttribute("totalApplications", jobApplicationService.getTotalApplications());
        } else if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_COMPANY"))) {
            Company company = companyService.findByUsername(username);
            model.addAttribute("totalActiveJobs", jobService.countActiveJobsByCompany(company));
            model.addAttribute("totalApplications", jobApplicationService.countApplicationsByCompany(company));
        }
        return "admin/dashboard/index";
    }
}
