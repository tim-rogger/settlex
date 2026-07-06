package ch.ledgerflow.notification.messaging;

import ch.ledgerflow.notification.domain.Notification;
import ch.ledgerflow.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"ledgerflow.settlements"})
class SettlementEventListenerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private NotificationRepository notifications;

    @Test
    void settlementEventCreatesNotification() {
        String payload = """
                {
                  "settlementId": 42,
                  "orderId": 7,
                  "transactionId": "tx-abc-123",
                  "amount": 100.00,
                  "currency": "CHF",
                  "status": "SETTLED"
                }
                """;

        kafkaTemplate.send("ledgerflow.settlements", payload);

        await().atMost(Duration.ofSeconds(15)).untilAsserted(() -> {
            List<Notification> stored = notifications.findAll();
            assertThat(stored)
                    .anySatisfy(n -> {
                        assertThat(n.getType()).isEqualTo("SETTLEMENT");
                        assertThat(n.getRecipient()).isEqualTo("order:7");
                        assertThat(n.getMessage()).contains("Settlement 42");
                        assertThat(n.getMessage()).contains("order 7");
                        assertThat(n.getMessage()).contains("SETTLED");
                        assertThat(n.getMessage()).contains("CHF");
                        assertThat(n.getMessage()).contains("tx-abc-123");
                    });
        });
    }
}
