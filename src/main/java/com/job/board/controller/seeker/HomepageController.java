package com.job.board.controller.seeker;

import com.job.board.entity.Job;
import com.job.board.enums.JobStatus;
import com.job.board.repository.JobCategoryRepository;
import com.job.board.repository.JobRepository;
import com.job.board.service.JobCategoryService;
import com.job.board.service.JobService;
import com.job.board.service.JobTagService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/")
@Controller
public class HomepageController {
    private final JobRepository jobRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final JobCategoryService jobCategoryService;
    private final JobService jobService;
    private final JobTagService jobTagService;

    public HomepageController(JobRepository jobRepository, JobCategoryRepository jobCategoryRepository, JobCategoryService jobCategoryService, JobService jobService, JobTagService jobTagService) {
        this.jobRepository = jobRepository;
        this.jobCategoryRepository = jobCategoryRepository;
        this.jobCategoryService = jobCategoryService;
        this.jobService = jobService;
        this.jobTagService = jobTagService;
    }

    @GetMapping
    public String homepage(
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

        model.addAttribute("listLocation", listLocation);
        model.addAttribute("listCategory", listCategory);
        model.addAttribute("listTag", listTag);
        model.addAttribute("pageJob", pageJob);
        return "/public/homepage/home";
    }
}
