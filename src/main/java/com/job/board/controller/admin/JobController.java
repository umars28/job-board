package com.job.board.controller.admin;

import com.job.board.entity.Job;
import com.job.board.enums.JobStatus;
import com.job.board.repository.CompanyRepository;
import com.job.board.repository.JobCategoryRepository;
import com.job.board.repository.JobRepository;
import com.job.board.repository.JobTagRepository;
import com.job.board.service.CompanyService;
import com.job.board.service.JobCategoryService;
import com.job.board.service.JobService;
import com.job.board.service.JobTagService;
import com.job.board.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequestMapping("/jobs")
@Controller("adminJobController")
public class JobController {

    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private final JobService jobService;
    private final JobTagRepository jobTagRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;
    private final JobTagService jobTagService;
    private final JobCategoryService jobCategoryService;
    private final AuthUtil authUtil;
    private JobRepository jobRepository;

    public JobController(JobRepository jobRepository, JobService jobService, JobTagRepository jobTagRepository, JobCategoryRepository jobCategoryRepository, CompanyRepository companyRepository, CompanyService companyService, JobTagService jobTagService, JobCategoryService jobCategoryService, AuthUtil authUtil) {
        this.jobRepository = jobRepository;
        this.jobService = jobService;
        this.jobTagRepository = jobTagRepository;
        this.jobCategoryRepository = jobCategoryRepository;
        this.companyRepository = companyRepository;
        this.companyService = companyService;
        this.jobTagService = jobTagService;
        this.jobCategoryService = jobCategoryService;
        this.authUtil = authUtil;
    }

    @GetMapping("/list")
    public String list(@RequestParam(required = false) String status, Model model) {
        List<Job> jobs = jobService.getJobsFiltered(status);
        model.addAttribute("jobs", jobs);
        model.addAttribute("statuses", JobStatus.values());
        model.addAttribute("selectedStatus", status);
        return "/admin/job/index";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        Job job = new Job();
        model.addAttribute("job", job);
        model.addAttribute("statuses", JobStatus.values());
        model.addAttribute("categories", jobCategoryService.getAllJobCategories());
        model.addAttribute("tags", jobTagService.getAllJobTags());
        model.addAttribute("companies", companyService.getAllCompanies());

        return "/admin/job/create";
    }

    @PostMapping("/save")
    public String saveJob(@ModelAttribute("job") Job job, Model model, RedirectAttributes redirectAttributes) {
        auditLogger.info("AUDIT - Request POST /jobs/save received to create a new job with title='{}'", job.getTitle());
        try {
            jobService.saveJob(job);
            redirectAttributes.addFlashAttribute("message", "Job berhasil dibuat.");
            return "redirect:/jobs/list";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Gagal membuat job: " + e.getMessage());
            model.addAttribute("job", job);
            model.addAttribute("statuses", JobStatus.values());
            model.addAttribute("categories", jobCategoryService.getAllJobCategories());
            model.addAttribute("tags", jobTagService.getAllJobTags());
            model.addAttribute("companies", companyService.getAllCompanies());
            return "/admin/job/create";
        }
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Job job = jobService.getJobById(id);
        authUtil.authorizeCompanyAccessToJob(job);
        model.addAttribute("job", job);
        model.addAttribute("statuses", JobStatus.values());
        model.addAttribute("categories", jobCategoryService.getAllJobCategories());
        model.addAttribute("tags", jobTagService.getAllJobTags());
        model.addAttribute("companies", companyService.getAllCompanies());

        return "/admin/job/edit";
    }

    @PostMapping("/update/{id}")
    public String updateJob(@PathVariable Long id, @ModelAttribute("job") Job job, Model model, RedirectAttributes redirectAttributes) {
        auditLogger.info("AUDIT - Request POST /jobs/update/{} received to update job with new title='{}'", id, job.getTitle());
        try {
            jobService.updateJob(id, job);
            redirectAttributes.addFlashAttribute("message", "Job berhasil diupdate.");
            return "redirect:/jobs/list";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Gagal mengupdate job: " + e.getMessage());
            model.addAttribute("job", job);
            model.addAttribute("statuses", JobStatus.values());
            model.addAttribute("categories", jobCategoryService.getAllJobCategories());
            model.addAttribute("tags", jobTagService.getAllJobTags());
            model.addAttribute("companies", companyService.getAllCompanies());
            return "/admin/job/edit";
        }
    }

    @GetMapping("/archive/{id}")
    public String archive(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        auditLogger.info("AUDIT - Request GET /jobs/archive/{} received", id);
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
        auditLogger.info("AUDIT - Request GET /jobs/restore/{} received", id);
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
}
