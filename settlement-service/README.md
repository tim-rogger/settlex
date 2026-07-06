# settlement-service

Executes settlements **idempotently** and keeps an audit trail. It consumes order events from `ledgerflow.orders`, calls account-service over REST to perform the transfer, records a settlement keyed by a unique idempotency key, and publishes the result to `ledgerflow.settlements`.

- **Port:** 8083
- **Database:** H2 in-memory by default (PostgreSQL via the `postgres` profile)
- **Consumes:** `ledgerflow.orders`
- **Publishes:** `ledgerflow.settlements`
- **Calls:** account-service `POST /transfers` (base URL via `ACCOUNT_BASE_URL`, default `http://localhost:8081`)
- **API docs:** http://localhost:8083/swagger-ui.html

## Endpoints

| Method | Path | Description |
|---|---|---|
| `POST` | `/settlements` | Settle an order (idempotent; safe to replay) |
| `GET` | `/settlements/{id}` | Get a settlement |

Replaying the same idempotency key returns the existing settlement instead of moving money twice. A failed transfer is recorded as a failed settlement rather than lost.

## Run

```bash
mvn -B -f pom.xml clean verify
java -jar target/settlement-service-0.1.0-SNAPSHOT.jar
```

The Kafka bootstrap address defaults to `localhost:9092` (`SPRING_KAFKA_BOOTSTRAP_SERVERS`).
