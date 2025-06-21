package com.job.board.service;

import com.job.board.config.RabbitMQProperties;
import com.job.board.model.NotificationPayload;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties props;

    public NotificationPublisher(RabbitTemplate rabbitTemplate, RabbitMQProperties props) {
        this.rabbitTemplate = rabbitTemplate;
        this.props = props;
    }

    public void notifyCompany(NotificationPayload payload) {
        rabbitTemplate.convertAndSend(props.getNotificationExchange(), props.getNotifyCompanyRoutingKey(), payload);
    }

    public void notifyJobSeeker(NotificationPayload payload) {
        rabbitTemplate.convertAndSend(props.getNotificationExchange(), props.getNotifyJobSeekerRoutingKey(), payload);
    }
}

