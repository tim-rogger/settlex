package ch.ledgerflow.account.repository;

import ch.ledgerflow.account.domain.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
    List<LedgerEntry> findByAccountIdOrderByCreatedAtAsc(Long accountId);
}
