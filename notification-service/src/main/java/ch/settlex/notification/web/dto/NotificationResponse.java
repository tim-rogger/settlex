package ch.settlex.notification.web.dto;

import ch.settlex.notification.domain.Notification;

import java.time.Instant;

public record NotificationResponse(
        Long id,
        String type,
        String message,
        String recipient,
        boolean read,
        Instant createdAt
) {
    public static NotificationResponse from(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getMessage(),
                notification.getRecipient(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
