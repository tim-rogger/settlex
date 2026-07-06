package ch.ledgerflow.order.repository;

import ch.ledgerflow.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
