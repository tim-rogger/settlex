package ch.ledgerflow.notification.messaging;

import ch.ledgerflow.notification.service.NotificationService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SettlementEventListener {

    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    public SettlementEventListener(NotificationService notificationService, ObjectMapper objectMapper) {
        this.notificationService = notificationService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "ledgerflow.settlements", groupId = "notification-service")
    public void onSettlement(String payload) throws Exception {
        JsonNode event = objectMapper.readTree(payload);
        long orderId = event.path("orderId").asLong();
        long settlementId = event.path("settlementId").asLong();
        String transactionId = event.path("transactionId").asText();
        String amount = event.path("amount").asText();
        String currency = event.path("currency").asText();
        String status = event.path("status").asText();

        String message = "Settlement " + settlementId + " for order " + orderId
                + " is " + status + " (" + amount + " " + currency
                + ", transaction " + transactionId + ")";

        notificationService.create("SETTLEMENT", message, "order:" + orderId);
    }
}
