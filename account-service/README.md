# account-service

Accounts and the **double-entry ledger** at the core of LedgerFlow. Holds account balances as `BigDecimal`, executes transfers, and protects concurrent updates with optimistic locking (`@Version`). This is the only service that moves money.

- **Port:** 8081
- **Database:** H2 in-memory by default (PostgreSQL via the `postgres` profile)
- **Dashboard:** http://localhost:8081
- **API docs:** http://localhost:8081/swagger-ui.html

## Endpoints

| Method | Path | Description |
|---|---|---|
| `POST` | `/accounts` | Create an account (owner, currency) |
| `GET` | `/accounts` | List all accounts |
| `GET` | `/accounts/{id}` | Get one account |
| `POST` | `/accounts/{id}/deposit` | Deposit funds |
| `POST` | `/transfers` | Execute a transfer between two accounts (idempotent, balanced ledger entries) |

Transfers require an `idempotencyKey`; funds are debited from the source and credited to the target as matched ledger entries. Insufficient funds and currency mismatches are rejected as `ProblemDetail` responses.

## Run

```bash
mvn -B -f pom.xml clean verify
java -jar target/account-service-0.1.0-SNAPSHOT.jar
```
