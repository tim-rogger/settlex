package ch.settlex.settlement.error;

public class SettlementNotFoundException extends RuntimeException {
    public SettlementNotFoundException(Long id) {
        super("Settlement not found: " + id);
    }
}
