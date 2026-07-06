package ch.ledgerflow.order.service;

import ch.ledgerflow.order.domain.Order;
import ch.ledgerflow.order.error.OrderNotFoundException;
import ch.ledgerflow.order.messaging.OrderEventPublisher;
import ch.ledgerflow.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class OrderService {

    private final OrderRepository orders;
    private final OrderEventPublisher publisher;

    public OrderService(OrderRepository orders, OrderEventPublisher publisher) {
        this.orders = orders;
        this.publisher = publisher;
    }

    @Transactional
    public Order createOrder(Long sourceAccountId, Long targetAccountId, BigDecimal amount, String currency) {
        Order saved = orders.save(new Order(sourceAccountId, targetAccountId, amount, currency));
        publisher.publishOrderCreated(saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public Order getOrder(Long id) {
        return orders.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
}
