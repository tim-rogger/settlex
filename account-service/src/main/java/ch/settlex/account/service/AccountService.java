package ch.settlex.account.service;

import ch.settlex.account.domain.Account;
import ch.settlex.account.error.AccountNotFoundException;
import ch.settlex.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accounts;

    public AccountService(AccountRepository accounts) {
        this.accounts = accounts;
    }

    @Transactional
    public Account createAccount(String ownerName, String currency) {
        return accounts.save(new Account(ownerName, currency));
    }

    @Transactional(readOnly = true)
    public Account getAccount(Long id) {
        return accounts.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    @Transactional
    public Account deposit(Long id, BigDecimal amount) {
        Account account = accounts.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        account.credit(amount);
        return accounts.save(account);
    }
}
