package com.job.board.controller;

import com.job.board.entity.Job;
import com.job.board.entity.JobApplication;
import com.job.board.enums.JobStatus;
import com.job.board.repository.JobRepository;
import com.job.board.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;
    private JobRepository jobRepository;

    public JobController(JobRepository jobRepository, JobService jobService) {
        this.jobRepository = jobRepository;
        this.jobService = jobService;
    }

    @GetMapping("/list")
    public String list(@RequestParam(required = false) String status, Model model) {
        List<Job> jobs;

        if (status != null) {
            JobStatus jobStatus = JobStatus.valueOf(status);
            jobs = jobService.getJobsByStatus(jobStatus);
            model.addAttribute("selectedStatus", status);
        } else {
            jobs = jobService.getAllJobs();
        }

        model.addAttribute("jobs", jobs);
        model.addAttribute("statuses", JobStatus.values());
        return "/admin/job/index";
    }

    @GetMapping("/archive/{id}")
    public String archive(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            jobService.archiveJob(id);
            redirectAttributes.addFlashAttribute("message", "Job archived successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to archive job: " + e.getMessage());
        }
        return "redirect:/jobs/list?status=" + JobStatus.ARCHIVED.name();
    }

    @GetMapping("/restore/{id}")
    public String restore(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            jobService.restoreJob(id);
            redirectAttributes.addFlashAttribute("message", "Job restored successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to restore job: " + e.getMessage());
        }
        return "redirect:/jobs/list?status=" + JobStatus.OPEN.name();
    }

    // @ModelAttribute explicit, can remove cause automatic, Binding data dari form, query param, path param ke objek Java
    // @RequestBody for json
    // @PathVariable Mengambil data dari URL path variable (dinamis pada URL)
    // @RequestParams Mengambil parameter dari query string atau form data
    @PostMapping
    public String save(@ModelAttribute Job job) {
        jobRepository.save(job);
        return "redirect:/jobs";
    }
}
