package ch.ledgerflow.settlement;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.web.client.RestClient;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"ledgerflow.orders", "ledgerflow.settlements"})
class SettlementServiceApplicationTests {

    @MockBean
    private RestClient accountRestClient;

    @Test
    void contextLoads() {
    }
}
