package ch.settlex.account.service;

import ch.settlex.account.domain.Account;
import ch.settlex.account.domain.Direction;
import ch.settlex.account.domain.LedgerEntry;
import ch.settlex.account.domain.Transfer;
import ch.settlex.account.error.AccountNotFoundException;
import ch.settlex.account.error.CurrencyMismatchException;
import ch.settlex.account.repository.AccountRepository;
import ch.settlex.account.repository.LedgerEntryRepository;
import ch.settlex.account.repository.TransferRepository;
import ch.settlex.account.web.dto.TransferRequest;
import ch.settlex.account.web.dto.TransferResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TransferService {

    private final AccountRepository accounts;
    private final LedgerEntryRepository ledgerEntries;
    private final TransferRepository transfers;

    public TransferService(AccountRepository accounts, LedgerEntryRepository ledgerEntries, TransferRepository transfers) {
        this.accounts = accounts;
        this.ledgerEntries = ledgerEntries;
        this.transfers = transfers;
    }

    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        return transfers.findByIdempotencyKey(request.idempotencyKey())
                .map(this::toResponse)
                .orElseGet(() -> executeTransfer(request));
    }

    private TransferResponse executeTransfer(TransferRequest request) {
        Account source = accounts.findById(request.sourceAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.sourceAccountId()));
        Account target = accounts.findById(request.targetAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.targetAccountId()));

        if (!source.getCurrency().equals(target.getCurrency())) {
            throw new CurrencyMismatchException(source.getCurrency(), target.getCurrency());
        }

        source.debit(request.amount());
        target.credit(request.amount());

        String transactionId = UUID.randomUUID().toString();

        ledgerEntries.save(new LedgerEntry(source.getId(), transactionId, Direction.DEBIT, request.amount(), request.description()));
        ledgerEntries.save(new LedgerEntry(target.getId(), transactionId, Direction.CREDIT, request.amount(), request.description()));

        Transfer transfer = transfers.save(new Transfer(
                request.idempotencyKey(),
                source.getId(),
                target.getId(),
                request.amount(),
                source.getCurrency(),
                transactionId));

        return toResponse(transfer);
    }

    private TransferResponse toResponse(Transfer transfer) {
        return new TransferResponse(
                transfer.getTransactionId(),
                transfer.getSourceAccountId(),
                transfer.getTargetAccountId(),
                transfer.getAmount(),
                transfer.getCurrency());
    }
}
