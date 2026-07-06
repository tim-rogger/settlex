package ch.ledgerflow.settlement.messaging;

import ch.ledgerflow.settlement.client.AccountClient;
import ch.ledgerflow.settlement.client.TransferRequest;
import ch.ledgerflow.settlement.client.TransferResponse;
import ch.ledgerflow.settlement.domain.Settlement;
import ch.ledgerflow.settlement.repository.SettlementRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"ledgerflow.orders", "ledgerflow.settlements"})
@TestPropertySource(properties = "spring.datasource.url=jdbc:h2:mem:settlement-listener;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE")
class OrderEventListenerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private SettlementRepository settlementRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountClient accountClient;

    @MockBean
    private RestClient accountRestClient;

    @BeforeEach
    void reset() {
        settlementRepository.deleteAll();
    }

    @Test
    void successfulTransferPersistsSettledSettlement() throws Exception {
        given(accountClient.transfer(any(TransferRequest.class)))
                .willReturn(new TransferResponse("txn-123", 1L, 2L, new BigDecimal("25.0000"), "CHF"));

        String message = objectMapper.writeValueAsString(new OrderEvent(
                100L, 1L, 2L, new BigDecimal("25.0000"), "CHF", "order-100"));
        kafkaTemplate.send(OrderEventListener.TOPIC, "100", message);

        await().atMost(Duration.ofSeconds(20)).untilAsserted(() -> {
            Optional<Settlement> saved = settlementRepository.findByIdempotencyKey("order-100");
            assertThat(saved).isPresent();
            assertThat(saved.get().getStatus()).isEqualTo("SETTLED");
            assertThat(saved.get().getTransactionId()).isEqualTo("txn-123");
            assertThat(saved.get().getOrderId()).isEqualTo(100L);
        });
    }

    @Test
    void failedTransferPersistsFailedSettlement() throws Exception {
        when(accountClient.transfer(any(TransferRequest.class)))
                .thenThrow(new RuntimeException("account-service unavailable"));

        String message = objectMapper.writeValueAsString(new OrderEvent(
                200L, 1L, 2L, new BigDecimal("40.0000"), "EUR", "order-200"));
        kafkaTemplate.send(OrderEventListener.TOPIC, "200", message);

        await().atMost(Duration.ofSeconds(20)).untilAsserted(() -> {
            Optional<Settlement> saved = settlementRepository.findByIdempotencyKey("order-200");
            assertThat(saved).isPresent();
            assertThat(saved.get().getStatus()).isEqualTo("FAILED");
        });
    }
}
