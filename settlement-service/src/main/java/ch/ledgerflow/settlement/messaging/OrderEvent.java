package ch.ledgerflow.settlement.messaging;

import java.math.BigDecimal;

public record OrderEvent(
        Long orderId,
        Long sourceAccountId,
        Long targetAccountId,
        BigDecimal amount,
        String currency,
        String idempotencyKey) {
}
