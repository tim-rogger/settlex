package ch.ledgerflow.order.web.dto;

import ch.ledgerflow.order.domain.Order;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderResponse(
        Long id,
        Long sourceAccountId,
        Long targetAccountId,
        BigDecimal amount,
        String currency,
        String status,
        Instant createdAt) {

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getSourceAccountId(),
                order.getTargetAccountId(),
                order.getAmount(),
                order.getCurrency(),
                order.getStatus(),
                order.getCreatedAt());
    }
}
