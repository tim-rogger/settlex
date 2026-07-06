package ch.ledgerflow.notification.web;

import ch.ledgerflow.notification.service.NotificationService;
import ch.ledgerflow.notification.web.dto.CreateNotificationRequest;
import ch.ledgerflow.notification.web.dto.NotificationResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificationResponse create(@Valid @RequestBody CreateNotificationRequest request) {
        return NotificationResponse.from(
                notificationService.create(request.type(), request.message(), request.recipient()));
    }

    @GetMapping("/unread")
    public List<NotificationResponse> listUnread() {
        return notificationService.listUnread().stream()
                .map(NotificationResponse::from)
                .toList();
    }

    @PostMapping("/{id}/read")
    @ResponseStatus(HttpStatus.OK)
    public NotificationResponse markRead(@PathVariable Long id) {
        return NotificationResponse.from(notificationService.markRead(id));
    }
}
