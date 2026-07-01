package ch.settlex.notification.service;

import ch.settlex.notification.domain.Notification;
import ch.settlex.notification.error.NotificationNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Test
    void createThenListUnreadReturnsIt() {
        Notification created = notificationService.create("ORDER", "Order filled", "alice@settlex.ch");

        List<Notification> unread = notificationService.listUnread();

        assertThat(unread).extracting(Notification::getId).contains(created.getId());
        assertThat(created.isRead()).isFalse();
    }

    @Test
    void markReadFlipsRead() {
        Notification created = notificationService.create("ORDER", "Order filled", "alice@settlex.ch");

        Notification updated = notificationService.markRead(created.getId());

        assertThat(updated.isRead()).isTrue();
        assertThat(notificationService.listUnread())
                .extracting(Notification::getId)
                .doesNotContain(created.getId());
    }

    @Test
    void markReadMissingThrows() {
        assertThatThrownBy(() -> notificationService.markRead(999999L))
                .isInstanceOf(NotificationNotFoundException.class);
    }
}
