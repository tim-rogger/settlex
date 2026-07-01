package ch.settlex.settlement.service;

import ch.settlex.settlement.domain.Settlement;
import ch.settlex.settlement.error.SettlementNotFoundException;
import ch.settlex.settlement.repository.SettlementRepository;
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
        return settlements.findByIdempotencyKey(idempotencyKey)
                .orElseGet(() -> settlements.save(
                        new Settlement(idempotencyKey, orderId, UUID.randomUUID().toString(), amount, currency)));
    }

    @Transactional(readOnly = true)
    public Settlement getSettlement(Long id) {
        return settlements.findById(id)
                .orElseThrow(() -> new SettlementNotFoundException(id));
    }
}
