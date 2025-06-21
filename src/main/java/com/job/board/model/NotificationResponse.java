package com.job.board.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponse {
    private Long id;
    private String message;
    private String link;
    private Boolean isRead;
    private String receiverUsername;
    private LocalDateTime createdAt;
}

