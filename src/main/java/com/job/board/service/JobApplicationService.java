package com.job.board.service;

import com.job.board.entity.*;
import com.job.board.enums.ApplicantStatus;
import com.job.board.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobApplicationService {
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");

    private final JobApplicationRepository jobApplicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    private final SeekerRepository seekerRepository;
    private final NotificationService notificationService;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository, UserRepository userRepository, JobRepository jobRepository, SeekerRepository seekerRepository, NotificationService notificationService) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
        this.seekerRepository = seekerRepository;
        this.notificationService = notificationService;
    }

    public List<JobApplication> getApplicantsByJobId(Long id) {
       return jobApplicationRepository.findByJobId(id);
    }

    public List<JobApplication> getApplicantByStatus(Long id, ApplicantStatus status) {
        return jobApplicationRepository.findByJobIdAndApplicantStatus(id, status);
    }

    @Transactional
    public JobApplication updateApplicationStatus(Long applicationId, ApplicantStatus newStatus) {
        JobApplication application = jobApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        ApplicantStatus oldStatus = application.getApplicantStatus();
        application.setApplicantStatus(newStatus);
        JobApplication updatedApplication = jobApplicationRepository.save(application);

        auditLogger.info(
                "AUDIT - Updated application id={} status from {} to {}",
                applicationId,
                oldStatus.name(),
                newStatus.name()
        );

        notificationService.notifyJobSeekerStatusChanged(updatedApplication);
        return updatedApplication;
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

    public List<JobApplication> getJobApplicationsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return jobApplicationRepository.findByJobSeekerId(user.getJobSeeker().getId());
    }

    @Transactional
    public void applyForJob(Long jobId, String username) {
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        JobSeeker seeker = seekerRepository.findByUserUsername(username)
                .orElseThrow(() -> new RuntimeException("Job seeker not found"));

        if (jobApplicationRepository.existsByJobIdAndJobSeekerId(jobId, seeker.getId())) {
            throw new RuntimeException("You have already applied for this job");
        }

        JobApplication application = new JobApplication();
        application.setJob(job);
        application.setJobSeeker(seeker);
        application.setApplicantStatus(ApplicantStatus.APPLIED);
        application.setAppliedAt(LocalDateTime.now());

        jobApplicationRepository.save(application);
        auditLogger.info(
                "AUDIT - Job application created for jobId={} by seekerId={}",
                jobId,
                seeker.getId()
        );
        notificationService.notifyCompanyJobApplied(job, seeker);
    }
}
