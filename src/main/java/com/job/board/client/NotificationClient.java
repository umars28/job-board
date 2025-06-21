package com.job.board.client;

import com.job.board.common.ApiResponse;
import com.job.board.model.NotificationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class NotificationClient {

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
}
