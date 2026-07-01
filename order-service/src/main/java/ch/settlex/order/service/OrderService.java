package ch.settlex.order.service;

import ch.settlex.order.domain.Order;
import ch.settlex.order.error.OrderNotFoundException;
import ch.settlex.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orders;

    public OrderService(OrderRepository orders) {
        this.orders = orders;
    }

    @Transactional
    public Order createOrder(Long sourceAccountId, Long targetAccountId, BigDecimal amount, String currency) {
        return orders.save(new Order(sourceAccountId, targetAccountId, amount, currency));
    }

    @Transactional(readOnly = true)
    public Order getOrder(Long id) {
        return orders.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
}
