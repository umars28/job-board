package com.job.board.controller.seeker;

import com.job.board.entity.Job;
import com.job.board.service.JobApplicationService;
import com.job.board.service.JobCategoryService;
import com.job.board.service.JobService;
import com.job.board.service.JobTagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/job")
@Controller("seekerJobController")
public class JobController {
    private final JobService jobService;
    private final JobCategoryService jobCategoryService;
    private final JobTagService jobTagService;
    private final JobApplicationService jobApplicationService;

    public JobController(JobService jobService, JobCategoryService jobCategoryService, JobTagService jobTagService, JobApplicationService jobApplicationService) {
        this.jobService = jobService;
        this.jobCategoryService = jobCategoryService;
        this.jobTagService = jobTagService;
        this.jobApplicationService = jobApplicationService;
    }

    @GetMapping("/list")
    public String jobList(
            @ModelAttribute("currentJobSeekerId") Long jobSeekerId,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) List<String> tags,
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 20, sort = "postedAt", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        List<String> listLocation = jobService.getDistinctLocations();
        List<String> listCategory = jobCategoryService.getCategoriesWithJobs();
        List<String> listTag = jobTagService.getTagsWithJobs();
        Page<Job> pageJob = jobService.findOpenJobs(location, category, tags, keyword, pageable);
        List<Long> appliedJobIds = jobApplicationService.findAppliedJobIdsByJobSeeker(jobSeekerId);

        model.addAttribute("listLocation", listLocation);
        model.addAttribute("listCategory", listCategory);
        model.addAttribute("listTag", listTag);
        model.addAttribute("pageJob", pageJob);
        model.addAttribute("appliedJobIds", appliedJobIds);
        return "/public/job/list";
    }

    @GetMapping("/detail/{id}")
    public String jobDetail(
            @ModelAttribute("currentJobSeekerId") Long jobSeekerId,
            @PathVariable Long id,
            Model model
    ) {
        Job job = jobService.getJobById(id);
        List<Long> appliedJobIds = jobApplicationService.findAppliedJobIdsByJobSeeker(jobSeekerId);
        model.addAttribute("job", job);
        model.addAttribute("appliedJobIds", appliedJobIds);
        return "/public/job/detail";
    }

    @PostMapping("/apply/{id}")
    public String applyJob(@PathVariable Long id, Principal principal) {
        String username = principal.getName();
        jobApplicationService.applyForJob(id, username);
        return "redirect:/job/apply/success?successApply=true";
    }

    @GetMapping("/apply/success")
    public String applySuccess(@RequestParam(value = "successApply", required = false, defaultValue = "false") boolean success) {
        if (!success) {
            return "redirect:/";
        }
        return "/public/feedback/success-apply";
    }
}
