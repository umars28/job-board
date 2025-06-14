package com.job.board.service;

import com.job.board.entity.Company;
import com.job.board.entity.Job;
import com.job.board.entity.JobApplication;
import com.job.board.enums.JobStatus;
import com.job.board.repository.CompanyRepository;
import com.job.board.repository.JobCategoryRepository;
import com.job.board.repository.JobRepository;
import com.job.board.util.AuthUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import static com.job.board.repository.specification.JobSpecification.*;

import java.util.Collections;
import java.util.List;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final JobCategoryRepository jobCategoryRepository;
    private final CompanyRepository companyRepository;
    private final CompanyService companyService;
    private final AuthUtil authUtil;

    public JobService(JobRepository jobRepository, JobCategoryRepository jobCategoryRepository, CompanyRepository companyRepository, CompanyService companyService, AuthUtil authUtil) {
        this.jobRepository = jobRepository;
        this.jobCategoryRepository = jobCategoryRepository;
        this.companyRepository = companyRepository;
        this.companyService = companyService;
        this.authUtil = authUtil;
    }

    public List<Job> getJobsFiltered(String status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        boolean isCompany = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role -> role.equals("ROLE_COMPANY"));

        JobStatus jobStatus = null;
        if (status != null) {
            jobStatus = JobStatus.valueOf(status);
        }

        if (isCompany) {
            if (jobStatus != null) {
                return jobRepository.findByCompanyUsernameAndStatusWithDetails(username, jobStatus);
            }
            return jobRepository.findByCompanyUsernameWithDetails(username);
        } else {
            if (jobStatus != null) {
                return jobRepository.findByStatusWithDetails(jobStatus);
            }
            return jobRepository.findAllWithDetails();
        }
    }

    public Job jobDetails(Long id) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));
        return job;
    }

    public void archiveJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
        authUtil.authorizeCompanyAccessToJob(job);
        job.setStatus(JobStatus.ARCHIVED);
        jobRepository.save(job);
    }

    public void restoreJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
        authUtil.authorizeCompanyAccessToJob(job);
        job.setStatus(JobStatus.OPEN);
        jobRepository.save(job);
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));
    }

    public void saveJob(Job job) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Company company = companyService.findByUsername(username);
        job.setCompany(company);
        jobRepository.save(job);
    }

    public void updateJob(Long id, Job updatedJob) {
        Job existingJob = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));

        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setStatus(updatedJob.getStatus());
        existingJob.setPostedAt(updatedJob.getPostedAt());
        existingJob.setExpiredAt(updatedJob.getExpiredAt());

        if (updatedJob.getCategory() != null && updatedJob.getCategory().getId() != null) {
            jobCategoryRepository.findById(updatedJob.getCategory().getId())
                    .ifPresent(existingJob::setCategory);
        } else {
            existingJob.setCategory(null);
        }

        existingJob.setTags(updatedJob.getTags());

        jobRepository.save(existingJob);
    }

    public long getTotalActiveJobPostings() {
        return jobRepository.countAllByStatus((JobStatus.OPEN));
    }

    public int countActiveJobsByCompany(Company company) {
        return jobRepository.countByCompanyAndStatus(company, JobStatus.OPEN);
    }

    public List<String> getDistinctLocations() {
        return jobRepository.findDistinctLocations();
    }

    public Page<Job> findOpenJobs(String location, String category, List<String> tags, String keyword, Pageable pageable) {
        Specification<Job> spec = (root, query, cb) -> cb.conjunction();

        spec = spec.and(statusOpen());

        if (location != null && !location.isBlank()) {
            spec = spec.and(locationEqual(location));
        }
        if (category != null && !category.isBlank()) {
            spec = spec.and(categoryEqual(category));
        }
        if (tags != null && !tags.isEmpty()) {
            spec = spec.and(tagsIn(tags));
        }
        if (keyword != null && !keyword.isBlank()) {
            spec = spec.and(keywordLike(keyword));
        }

        return jobRepository.findAll(spec, pageable);
    }

}
