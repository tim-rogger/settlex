package ch.ledgerflow.settlement.service;

import ch.ledgerflow.settlement.domain.Settlement;
import ch.ledgerflow.settlement.error.SettlementNotFoundException;
import ch.ledgerflow.settlement.repository.SettlementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class SettlementService {

    private final SettlementRepository settlements;

    public SettlementService(SettlementRepository settlements) {
        this.settlements = settlements;
    }

    @Transactional
    public Settlement settle(String idempotencyKey, Long orderId, BigDecimal amount, String currency) {
        return settle(idempotencyKey, orderId, UUID.randomUUID().toString(), amount, currency);
    }

    @Transactional
    public Settlement settle(String idempotencyKey, Long orderId, String transactionId, BigDecimal amount, String currency) {
        return settlements.findByIdempotencyKey(idempotencyKey)
                .orElseGet(() -> settlements.save(
                        new Settlement(idempotencyKey, orderId, transactionId, amount, currency, "SETTLED")));
    }

    @Transactional
    public Settlement settleFailed(String idempotencyKey, Long orderId, BigDecimal amount, String currency) {
        return settlements.findByIdempotencyKey(idempotencyKey)
                .orElseGet(() -> settlements.save(
                        new Settlement(idempotencyKey, orderId, UUID.randomUUID().toString(), amount, currency, "FAILED")));
    }

    @Transactional(readOnly = true)
    public Settlement getSettlement(Long id) {
        return settlements.findById(id)
                .orElseThrow(() -> new SettlementNotFoundException(id));
    }
}
