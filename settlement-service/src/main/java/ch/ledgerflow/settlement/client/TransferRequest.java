package ch.ledgerflow.settlement.client;

import java.math.BigDecimal;

public record TransferRequest(
        String idempotencyKey,
        Long sourceAccountId,
        Long targetAccountId,
        BigDecimal amount,
        String description) {
}
