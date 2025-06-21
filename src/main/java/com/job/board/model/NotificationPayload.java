package com.job.board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NotificationPayload implements Serializable {
    private String receiverUsername;
    private String message;
    private String link;

    @Override
    public String toString() {
        return "NotificationPayload{" +
                "title='" + receiverUsername + '\'' +
                ", message='" + message + '\'' +
                ", link=" + link +
                '}';
    }
}
