package ch.ledgerflow.account.repository;

import ch.ledgerflow.account.domain.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    Optional<Transfer> findByIdempotencyKey(String idempotencyKey);
}
