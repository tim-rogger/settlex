package ch.ledgerflow.settlement.client;

import java.math.BigDecimal;

public record TransferResponse(
        String transactionId,
        Long sourceAccountId,
        Long targetAccountId,
        BigDecimal amount,
        String currency) {
}
