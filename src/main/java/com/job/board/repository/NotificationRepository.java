package com.job.board.repository;

import com.job.board.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findTop5ByReceiverUsernameOrderByCreatedAtDesc(String username);

    long countByReceiverUsernameAndIsReadFalse(String username);

    List<Notification> findByReceiverUsernameOrderByCreatedAtDesc(String username);

    List<Notification> findByReceiverUsernameAndIsReadFalse(String username);
}
