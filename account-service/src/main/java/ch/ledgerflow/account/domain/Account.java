package ch.ledgerflow.account.domain;

import ch.ledgerflow.account.error.InsufficientFundsException;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;


@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ownerName;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Version
    private Long version;

    @Column(nullable = false)
    private Instant createdAt;

    protected Account() {
    }

    public Account(String ownerName, String currency) {
        this.ownerName = ownerName;
        this.currency = currency;
        this.balance = BigDecimal.ZERO;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void debit(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException(id, balance, amount);
        }
        balance = balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        balance = balance.add(amount);
    }
}
