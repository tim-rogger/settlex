package ch.ledgerflow.settlement.messaging;

import ch.ledgerflow.settlement.domain.Settlement;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class SettlementEventPublisher {

    public static final String TOPIC = "ledgerflow.settlements";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public SettlementEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publish(Settlement settlement) {
        SettlementEvent event = new SettlementEvent(
                settlement.getId(),
                settlement.getOrderId(),
                settlement.getTransactionId(),
                settlement.getAmount(),
                settlement.getCurrency(),
                settlement.getStatus());
        try {
            kafkaTemplate.send(TOPIC, String.valueOf(settlement.getOrderId()), objectMapper.writeValueAsString(event));
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Failed to serialize settlement event", exception);
        }
    }
}
