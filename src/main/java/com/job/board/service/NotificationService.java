package com.job.board.service;

import com.job.board.entity.*;
import com.job.board.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void notifyCompanyJobApplied(Job job, JobSeeker seeker) {
        User companyUser = job.getCompany().getUser();
        String message = seeker.getFullName() + " applied for your job: " + job.getTitle();

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReceiver(companyUser);

        notificationRepository.save(notification);
    }

    public void notifyJobSeekerStatusChanged(JobApplication application) {
        User jobSeekerUser = application.getJobSeeker().getUser();
        String message = "Status for your application on '" + application.getJob().getTitle()
                + "' has been updated to: " + application.getApplicantStatus();

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReceiver(jobSeekerUser);

        notificationRepository.save(notification);
    }

}
