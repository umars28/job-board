package com.job.board.service;

import com.job.board.entity.*;
import com.job.board.enums.ApplicantStatus;
import com.job.board.enums.JobStatus;
import com.job.board.model.NotificationPayload;
import com.job.board.repository.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private final NotificationRepository notificationRepository;
    private final NotificationPublisher notificationPublisher;

    public NotificationService(NotificationRepository notificationRepository, NotificationPublisher notificationPublisher) {
        this.notificationRepository = notificationRepository;
        this.notificationPublisher = notificationPublisher;
    }

    public List<Notification> getAllNotifications(String username) {
        return notificationRepository.findByReceiverUsernameOrderByCreatedAtDesc(username);
    }

    public void markAllAsRead(String username) {
        List<Notification> unread = notificationRepository.findByReceiverUsernameAndIsReadFalse(username);
        unread.forEach(n -> n.setIsRead(true));
        notificationRepository.saveAll(unread);

        auditLogger.info(
                "AUDIT - Request POST /notification/mark-all-read for username={} ({} notifications marked as read)",
                username,
                unread.size()
        );
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

        Notification notification = new Notification();
        String link = "/seeker/job/applied";
        notification.setMessage(message);
        notification.setLink(link);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setReceiver(jobSeekerUser);

        notificationRepository.save(notification);

        NotificationPayload payload = new NotificationPayload(
                jobSeekerUser.getUsername(),
                message,
                link
        );
        notificationPublisher.notifyJobSeeker(payload);
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
        auditLogger.info(
                "AUDIT - Request GET /notification/redirect/{} (marked as read, receiver username={})",
                id,
                notification.getReceiver().getUsername()
        );
        return notification;
    }

}
