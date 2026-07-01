# SettleX

Payment & settlement ledger platform — a small event-driven microservice system
demonstrating correct money handling (double-entry ledger, optimistic locking,
idempotency) with **Java 21** & **Spring Boot 3**.

> 🚧 Work in progress — built step by step.

## Services

| Service | Responsibility |
|---|---|
| `account-service` | Accounts & double-entry ledger, balances, transfers (optimistic locking) |
| `order-service` | Initiates payment orders |
| `settlement-service` | Executes transfers idempotently, writes an audit trail |
| `notification-service` | Reacts to settlement events |

## Tech stack

Java 21 · Spring Boot 3 · Spring Data JPA · Flyway · JUnit 5 · Maven
