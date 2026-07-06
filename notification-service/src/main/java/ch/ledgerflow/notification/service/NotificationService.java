package ch.ledgerflow.notification.service;

import ch.ledgerflow.notification.domain.Notification;
import ch.ledgerflow.notification.error.NotificationNotFoundException;
import ch.ledgerflow.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notifications;

    public NotificationService(NotificationRepository notifications) {
        this.notifications = notifications;
    }

    @Transactional
    public Notification create(String type, String message, String recipient) {
        return notifications.save(new Notification(type, message, recipient));
    }

    @Transactional(readOnly = true)
    public List<Notification> listUnread() {
        return notifications.findByReadFalseOrderByCreatedAtDesc();
    }

    @Transactional
    public Notification markRead(Long id) {
        Notification notification = notifications.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(id));
        notification.markRead();
        return notifications.save(notification);
    }
}
