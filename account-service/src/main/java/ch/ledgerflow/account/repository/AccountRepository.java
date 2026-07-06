package ch.ledgerflow.account.repository;

import ch.ledgerflow.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
