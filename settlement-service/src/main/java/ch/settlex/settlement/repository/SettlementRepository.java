package ch.settlex.settlement.repository;

import ch.settlex.settlement.domain.Settlement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettlementRepository extends JpaRepository<Settlement, Long> {

    Optional<Settlement> findByIdempotencyKey(String idempotencyKey);
}
