package ch.ledgerflow.settlement.messaging;

import java.math.BigDecimal;

public record SettlementEvent(
        Long settlementId,
        Long orderId,
        String transactionId,
        BigDecimal amount,
        String currency,
        String status) {
}
