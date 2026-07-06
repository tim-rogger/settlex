package ch.ledgerflow.notification.service;

import ch.ledgerflow.notification.domain.Notification;
import ch.ledgerflow.notification.error.NotificationNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"ledgerflow.settlements"})
@Transactional
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Test
    void createThenListUnreadReturnsIt() {
        Notification created = notificationService.create("ORDER", "Order filled", "alice@ledgerflow.ch");

        List<Notification> unread = notificationService.listUnread();

        assertThat(unread).extracting(Notification::getId).contains(created.getId());
        assertThat(created.isRead()).isFalse();
    }

    @Test
    void markReadFlipsRead() {
        Notification created = notificationService.create("ORDER", "Order filled", "alice@ledgerflow.ch");

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
