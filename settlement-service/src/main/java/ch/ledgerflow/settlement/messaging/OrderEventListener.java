package ch.ledgerflow.settlement.messaging;

import ch.ledgerflow.settlement.client.AccountClient;
import ch.ledgerflow.settlement.client.TransferRequest;
import ch.ledgerflow.settlement.client.TransferResponse;
import ch.ledgerflow.settlement.domain.Settlement;
import ch.ledgerflow.settlement.service.SettlementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventListener {

    public static final String TOPIC = "ledgerflow.orders";

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    private final ObjectMapper objectMapper;
    private final AccountClient accountClient;
    private final SettlementService settlementService;
    private final SettlementEventPublisher settlementEventPublisher;

    public OrderEventListener(ObjectMapper objectMapper,
                              AccountClient accountClient,
                              SettlementService settlementService,
                              SettlementEventPublisher settlementEventPublisher) {
        this.objectMapper = objectMapper;
        this.accountClient = accountClient;
        this.settlementService = settlementService;
        this.settlementEventPublisher = settlementEventPublisher;
    }

    @KafkaListener(topics = TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void onOrder(String message) {
        OrderEvent event;
        try {
            event = objectMapper.readValue(message, OrderEvent.class);
        } catch (Exception exception) {
            log.error("Failed to parse order event: {}", message, exception);
            return;
        }

        try {
            TransferResponse response = accountClient.transfer(new TransferRequest(
                    event.idempotencyKey(),
                    event.sourceAccountId(),
                    event.targetAccountId(),
                    event.amount(),
                    "settlement"));
            Settlement settlement;
            if (response != null && response.transactionId() != null && !response.transactionId().isBlank()) {
                settlement = settlementService.settle(
                        event.idempotencyKey(), event.orderId(), response.transactionId(), event.amount(), event.currency());
            } else {
                settlement = settlementService.settle(
                        event.idempotencyKey(), event.orderId(), event.amount(), event.currency());
            }
            settlementEventPublisher.publish(settlement);
        } catch (Exception exception) {
            log.error("Settlement failed for order {}", event.orderId(), exception);
            settlementService.settleFailed(
                    event.idempotencyKey(), event.orderId(), event.amount(), event.currency());
        }
    }
}
