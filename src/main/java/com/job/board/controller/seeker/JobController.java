package com.job.board.controller.seeker;

import com.job.board.entity.Job;
import com.job.board.service.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/job")
@Controller("seekerJobController")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/list")
    public String jobList() {
        return "/public/job/list";
    }

    @GetMapping("/detail/{id}")
    public String jobDetail(@PathVariable Long id, Model model) {
        Job job = jobService.getJobById(id);
        model.addAttribute("job", job);
        return "/public/job/detail";
    }
}
