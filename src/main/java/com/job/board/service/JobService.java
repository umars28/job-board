package com.job.board.service;

import com.job.board.entity.Job;
import com.job.board.entity.JobApplication;
import com.job.board.enums.JobStatus;
import com.job.board.repository.JobRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {
    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
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
}
