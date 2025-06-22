package com.job.board.service;

import com.job.board.client.NotificationClient;
import com.job.board.entity.*;
import com.job.board.enums.ApplicantStatus;
import com.job.board.enums.JobStatus;
import com.job.board.model.NotificationPayload;
import com.job.board.model.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private final NotificationPublisher notificationPublisher;
    private final NotificationClient notificationClient;

    public NotificationService(NotificationPublisher notificationPublisher, NotificationClient notificationClient) {
        this.notificationPublisher = notificationPublisher;
        this.notificationClient = notificationClient;
    }

    public List<NotificationResponse> getAllNotifications(String username) {
        return notificationClient.getAllNotifications(username);
    }

    public void notifyCompanyJobApplied(Job job, JobSeeker seeker) {
        User companyUser = job.getCompany().getUser();
        String message = seeker.getFullName() + " applied for your job: " + job.getTitle();
        String link = "/jobs/applications/" + job.getId() + "/applicants?status"+ ApplicantStatus.APPLIED;

        NotificationPayload payload = new NotificationPayload(
                companyUser.getUsername(),
                message,
                link
        );
        notificationPublisher.notifyCompany(payload);
    }

    public void notifyJobSeekerStatusChanged(JobApplication application) {
        User jobSeekerUser = application.getJobSeeker().getUser();
        String message = "Status for your application on '" + application.getJob().getTitle()
                + "' has been updated to: " + application.getApplicantStatus();
        String link = "/seeker/job/applied";

        NotificationPayload payload = new NotificationPayload(
                jobSeekerUser.getUsername(),
                message,
                link
        );
        notificationPublisher.notifyJobSeeker(payload);
    }

}
