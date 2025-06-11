package com.job.board.service;

import com.job.board.entity.Job;
import com.job.board.entity.JobApplication;
import com.job.board.enums.JobStatus;
import com.job.board.repository.JobCategoryRepository;
import com.job.board.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final JobCategoryRepository jobCategoryRepository;

    public JobService(JobRepository jobRepository, JobCategoryRepository jobCategoryRepository) {
        this.jobRepository = jobRepository;
        this.jobCategoryRepository = jobCategoryRepository;
    }

    public List<Job> getAllJobs(){
        return jobRepository.findAllWithDetails();
    }

    public List<Job> getJobsByStatus(JobStatus jobStatus) {
        return jobRepository.findByStatusWithDetails(jobStatus);
    }

    public Job jobDetails(Long id) {
        Job job = jobRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));
        return job;
    }

    public void archiveJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
        job.setStatus(JobStatus.ARCHIVED);
        jobRepository.save(job);
    }

    public void restoreJob(Long id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Job not found with id: " + id));
        job.setStatus(JobStatus.OPEN);
        jobRepository.save(job);
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid job ID: " + id));
    }

    public void saveJob(Job job) {
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
}
