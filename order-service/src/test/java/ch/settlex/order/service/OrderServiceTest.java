package ch.settlex.order.service;

import ch.settlex.order.domain.Order;
import ch.settlex.order.error.OrderNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Test
    void createOrderSavesPendingAndGetOrderReturnsIt() {
        Order created = orderService.createOrder(1L, 2L, new BigDecimal("100.0000"), "CHF");

        assertThat(created.getId()).isNotNull();
        assertThat(created.getStatus()).isEqualTo("PENDING");

        Order fetched = orderService.getOrder(created.getId());

        assertThat(fetched.getId()).isEqualTo(created.getId());
        assertThat(fetched.getSourceAccountId()).isEqualTo(1L);
        assertThat(fetched.getTargetAccountId()).isEqualTo(2L);
        assertThat(fetched.getAmount()).isEqualByComparingTo(new BigDecimal("100.0000"));
        assertThat(fetched.getCurrency()).isEqualTo("CHF");
        assertThat(fetched.getStatus()).isEqualTo("PENDING");
    }

    @Test
    void getOrderMissingThrows() {
        assertThatThrownBy(() -> orderService.getOrder(999999L))
                .isInstanceOf(OrderNotFoundException.class);
    }
}
