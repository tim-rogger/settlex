package ch.ledgerflow.settlement.service;

import ch.ledgerflow.settlement.domain.Settlement;
import ch.ledgerflow.settlement.repository.SettlementRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"ledgerflow.orders", "ledgerflow.settlements"})
@Transactional
class SettlementServiceTest {

    @Autowired
    private SettlementService settlementService;

    @Autowired
    private SettlementRepository settlementRepository;

    @MockBean
    private RestClient accountRestClient;

    @Test
    void settleCreatesSettledSettlement() {
        Settlement settlement = settlementService.settle("key-1", 42L, new BigDecimal("100.0000"), "CHF");

        assertThat(settlement.getId()).isNotNull();
        assertThat(settlement.getStatus()).isEqualTo("SETTLED");
        assertThat(settlement.getTransactionId()).isNotBlank();
        assertThat(settlement.getOrderId()).isEqualTo(42L);
        assertThat(settlement.getAmount()).isEqualByComparingTo(new BigDecimal("100.0000"));
        assertThat(settlement.getCurrency()).isEqualTo("CHF");
    }

    @Test
    void sameIdempotencyKeyReturnsSameTransactionAndOnlyOneRow() {
        Settlement first = settlementService.settle("key-2", 1L, new BigDecimal("50.0000"), "EUR");
        Settlement second = settlementService.settle("key-2", 1L, new BigDecimal("50.0000"), "EUR");

        assertThat(second.getId()).isEqualTo(first.getId());
        assertThat(second.getTransactionId()).isEqualTo(first.getTransactionId());
        assertThat(settlementRepository.findByIdempotencyKey("key-2")).isPresent();
        assertThat(settlementRepository.count()).isEqualTo(1);
    }
}
