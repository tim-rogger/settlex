# order-service

Initiates payment orders. When an order is created it is persisted and an event is published to the Kafka topic `ledgerflow.orders`, which the settlement-service consumes.

- **Port:** 8082
- **Database:** H2 in-memory by default (PostgreSQL via the `postgres` profile)
- **Publishes:** `ledgerflow.orders`
- **API docs:** http://localhost:8082/swagger-ui.html

## Endpoints

| Method | Path | Description |
|---|---|---|
| `POST` | `/orders` | Create a payment order (source, target, amount, currency) |
| `GET` | `/orders/{id}` | Get an order |

## Run

```bash
mvn -B -f pom.xml clean verify
java -jar target/order-service-0.1.0-SNAPSHOT.jar
```

The REST endpoints work without a running Kafka broker; event publishing needs one. The default Kafka bootstrap address is `localhost:9092`, overridable via `SPRING_KAFKA_BOOTSTRAP_SERVERS`.
