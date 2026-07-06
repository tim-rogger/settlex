package ch.ledgerflow.order;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"ledgerflow.orders"})
class OrderServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
