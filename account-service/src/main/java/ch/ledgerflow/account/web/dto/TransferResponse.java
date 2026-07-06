package ch.ledgerflow.account.web.dto;

import java.math.BigDecimal;

public record TransferResponse(
        String transactionId,
        Long sourceAccountId,
        Long targetAccountId,
        BigDecimal amount,
        String currency) {
}
