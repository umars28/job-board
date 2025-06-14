package com.job.board.service;

import com.job.board.entity.Company;
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

    public JobApplication updateApplicationStatus(Long applicationId, ApplicantStatus newStatus) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        application.setApplicantStatus(newStatus);
        return jobApplicationRepository.save(application);
    }

    public long getTotalApplications() {
        return jobApplicationRepository.count();
    }

    public int countApplicationsByCompany(Company company) {
        return jobApplicationRepository.countByJobCompany(company);
    }

    public List<Long> findAppliedJobIdsByJobSeeker(Long jobSeekerId) {
        return jobApplicationRepository.findJobIdsByJobSeekerId(jobSeekerId);
    }
}
