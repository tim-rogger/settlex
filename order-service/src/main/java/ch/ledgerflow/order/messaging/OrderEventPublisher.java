package ch.ledgerflow.order.messaging;

import ch.ledgerflow.order.domain.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderEventPublisher {

    public static final String TOPIC = "ledgerflow.orders";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public OrderEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public String publishOrderCreated(Order order) {
        String idempotencyKey = UUID.randomUUID().toString();
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("orderId", order.getId());
        payload.put("sourceAccountId", order.getSourceAccountId());
        payload.put("targetAccountId", order.getTargetAccountId());
        payload.put("amount", order.getAmount());
        payload.put("currency", order.getCurrency());
        payload.put("idempotencyKey", idempotencyKey);
        try {
            kafkaTemplate.send(TOPIC, String.valueOf(order.getId()), objectMapper.writeValueAsString(payload));
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize order event", e);
        }
        return idempotencyKey;
    }
}
