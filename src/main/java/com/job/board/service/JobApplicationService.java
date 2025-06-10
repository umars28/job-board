package com.job.board.service;

import com.job.board.entity.Job;
import com.job.board.entity.JobApplication;
import com.job.board.enums.ApplicantStatus;
import com.job.board.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
    }

    public List<JobApplication> getApplicantsByJobId(Long id) {
       return jobApplicationRepository.findByJobId(id);
    }

    public List<JobApplication> getApplicantByStatus(Long id, ApplicantStatus status) {
        return jobApplicationRepository.findByJobIdAndApplicantStatus(id, status);
    }
}
