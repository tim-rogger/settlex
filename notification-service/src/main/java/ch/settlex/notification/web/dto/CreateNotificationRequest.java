package ch.settlex.notification.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateNotificationRequest(
        @NotBlank String type,
        @NotBlank String message,
        @NotBlank String recipient
) {
}
