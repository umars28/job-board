package com.job.board.client;

import com.job.board.common.ApiResponse;
import com.job.board.model.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class NotificationClient {

    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    private final RestTemplate restTemplate;
    private final String baseUrl;

    public NotificationClient(RestTemplate restTemplate,
                              @Value("${notification-service.url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public List<NotificationResponse> getAllNotifications(String username) {
        String url = String.format("%s/api/notifications/all?username=%s", baseUrl, username);

        ResponseEntity<ApiResponse<List<NotificationResponse>>> response = restTemplate.exchange(
                url,
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        ApiResponse<List<NotificationResponse>> body = response.getBody();
        return body != null && body.getData() != null ? body.getData() : List.of();
    }

    public List<NotificationResponse> getLatestNotifications(String username) {
        String url = String.format("%s/api/notifications?username=%s", baseUrl, username);

        ResponseEntity<ApiResponse<List<NotificationResponse>>> response = restTemplate.exchange(
                url,
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        ApiResponse<List<NotificationResponse>> body = response.getBody();

        if (body != null && body.getData() != null) {
            return body.getData();
        } else {
            return List.of();
        }
    }

    public Long getUnreadCount(String username) {
        String url = String.format("%s/api/notifications/unread-count?username=%s", baseUrl, username);

        ResponseEntity<ApiResponse<Long>> response = restTemplate.exchange(
                url,
                org.springframework.http.HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        ApiResponse<Long> body = response.getBody();

        if (body != null && body.getData() != null) {
            return body.getData();
        } else {
            return 0L;
        }
    }

    public void markAllAsRead(String username) {
        String url = String.format("%s/api/notifications/mark-all-read?username=%s", baseUrl, username);
        restTemplate.postForEntity(url, null, ApiResponse.class);
        auditLogger.info(
                "AUDIT - Request POST /notification/mark-all-read for username={}",
                username
        );
    }

    public NotificationResponse markAsRead(Long id) {
        String url = String.format("%s/api/notifications/%d/mark-read", baseUrl, id);
        ApiResponse<NotificationResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                null,
                new ParameterizedTypeReference<ApiResponse<NotificationResponse>>() {}
        ).getBody();

        if (response != null && response.getData() != null) {
            NotificationResponse notification = response.getData();

            auditLogger.info(
                    "AUDIT - Request GET /notification/redirect/{} (marked as read, receiver username={})",
                    id,
                    notification.getReceiverUsername()
            );

            return notification;
        } else {
            throw new RuntimeException("Failed to mark notification as read");
        }
    }
}
