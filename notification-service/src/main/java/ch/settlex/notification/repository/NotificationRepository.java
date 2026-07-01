package ch.settlex.notification.repository;

import ch.settlex.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByReadFalseOrderByCreatedAtDesc();
}
