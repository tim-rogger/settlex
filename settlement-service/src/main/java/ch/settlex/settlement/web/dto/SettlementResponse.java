package ch.settlex.settlement.web.dto;

import ch.settlex.settlement.domain.Settlement;

import java.math.BigDecimal;
import java.time.Instant;

public record SettlementResponse(
        Long id,
        String idempotencyKey,
        Long orderId,
        String transactionId,
        BigDecimal amount,
        String currency,
        String status,
        Instant createdAt) {

    public static SettlementResponse from(Settlement settlement) {
        return new SettlementResponse(
                settlement.getId(),
                settlement.getIdempotencyKey(),
                settlement.getOrderId(),
                settlement.getTransactionId(),
                settlement.getAmount(),
                settlement.getCurrency(),
                settlement.getStatus(),
                settlement.getCreatedAt());
    }
}
