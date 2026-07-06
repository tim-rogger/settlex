package ch.ledgerflow.account.service;

import ch.ledgerflow.account.domain.Account;
import ch.ledgerflow.account.error.AccountNotFoundException;
import ch.ledgerflow.account.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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

    @Transactional(readOnly = true)
    public List<Account> listAccounts() {
        return accounts.findAll();
    }

    @Transactional
    public Account deposit(Long id, BigDecimal amount) {
        Account account = accounts.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));
        account.credit(amount);
        return accounts.save(account);
    }
}
