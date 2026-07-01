package ch.settlex.account.error;

public class CurrencyMismatchException extends RuntimeException {
    public CurrencyMismatchException(String sourceCurrency, String targetCurrency) {
        super("Currency mismatch: source " + sourceCurrency + ", target " + targetCurrency);
    }
}
