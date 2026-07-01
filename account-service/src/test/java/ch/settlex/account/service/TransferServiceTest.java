package ch.settlex.account.service;

import ch.settlex.account.domain.Account;
import ch.settlex.account.error.InsufficientFundsException;
import ch.settlex.account.repository.AccountRepository;
import ch.settlex.account.web.dto.TransferRequest;
import ch.settlex.account.web.dto.TransferResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class TransferServiceTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accounts;

    @Test
    void transfersFundsBetweenAccounts() {
        Account source = accountService.createAccount("Alice", "CHF");
        Account target = accountService.createAccount("Bob", "CHF");
        source.credit(new BigDecimal("100.0000"));
        accounts.save(source);

        transferService.transfer(new TransferRequest(
                "key-transfer-1", source.getId(), target.getId(), new BigDecimal("30.0000"), "rent"));

        assertThat(accounts.findById(source.getId()).orElseThrow().getBalance())
                .isEqualByComparingTo("70.0000");
        assertThat(accounts.findById(target.getId()).orElseThrow().getBalance())
                .isEqualByComparingTo("30.0000");
    }

    @Test
    void appliesTransferOnlyOnceForSameIdempotencyKey() {
        Account source = accountService.createAccount("Alice", "CHF");
        Account target = accountService.createAccount("Bob", "CHF");
        source.credit(new BigDecimal("100.0000"));
        accounts.save(source);

        TransferRequest request = new TransferRequest(
                "key-transfer-2", source.getId(), target.getId(), new BigDecimal("30.0000"), "rent");

        TransferResponse first = transferService.transfer(request);
        TransferResponse second = transferService.transfer(request);

        assertThat(second.transactionId()).isEqualTo(first.transactionId());
        assertThat(accounts.findById(source.getId()).orElseThrow().getBalance())
                .isEqualByComparingTo("70.0000");
        assertThat(accounts.findById(target.getId()).orElseThrow().getBalance())
                .isEqualByComparingTo("30.0000");
    }

    @Test
    void rejectsTransferWithInsufficientFunds() {
        Account source = accountService.createAccount("Alice", "CHF");
        Account target = accountService.createAccount("Bob", "CHF");

        TransferRequest request = new TransferRequest(
                "key-transfer-3", source.getId(), target.getId(), new BigDecimal("30.0000"), "rent");

        assertThatThrownBy(() -> transferService.transfer(request))
                .isInstanceOf(InsufficientFundsException.class);
    }
}
