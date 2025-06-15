package com.job.board.service;

import com.job.board.entity.*;
import com.job.board.enums.ApplicantStatus;
import com.job.board.enums.JobStatus;
import com.job.board.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getAllNotifications(String username) {
        return notificationRepository.findByReceiverUsernameOrderByCreatedAtDesc(username);
    }

    public void markAllAsRead(String username) {
        List<Notification> unread = notificationRepository.findByReceiverUsernameAndIsReadFalse(username);
        unread.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(unread);
    }

    public void notifyCompanyJobApplied(Job job, JobSeeker seeker) {
        User companyUser = job.getCompany().getUser();
        String message = seeker.getFullName() + " applied for your job: " + job.getTitle();

        Notification notification = new Notification();
        String link = "/jobs/applications/" + job.getId() + "/applicants?status"+ ApplicantStatus.APPLIED;
        notification.setMessage(message);
        notification.setLink(link);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReceiver(companyUser);

        notificationRepository.save(notification);
    }

    public void notifyJobSeekerStatusChanged(JobApplication application) {
        User jobSeekerUser = application.getJobSeeker().getUser();
        String message = "Status for your application on '" + application.getJob().getTitle()
                + "' has been updated to: " + application.getApplicantStatus();

        Notification notification = new Notification();
        String link = "/seeker/job/applied";
        notification.setMessage(message);
        notification.setLink(link);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReceiver(jobSeekerUser);

        notificationRepository.save(notification);
    }

    public List<Notification> getLatestNotifications(String username) {
        return notificationRepository.findTop5ByReceiverUsernameOrderByCreatedAtDesc(username);
    }

    public long countUnread(String username) {
        return notificationRepository.countByReceiverUsernameAndIsReadFalse(username);
    }

    public Notification markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getIsRead()) {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        }
        return notification;
    }

}
