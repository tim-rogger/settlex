package ch.ledgerflow.account.error;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(Long accountId, BigDecimal balance, BigDecimal amount) {
        super("Insufficient funds on account " + accountId + ": balance " + balance + ", requested " + amount);
    }
}
