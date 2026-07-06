# notification-service

Reacts to settlement events. It consumes `ledgerflow.settlements` and records a notification for each settled (or failed) order. Notifications can also be created and managed directly over REST.

- **Port:** 8084
- **Database:** H2 in-memory by default (PostgreSQL via the `postgres` profile)
- **Consumes:** `ledgerflow.settlements`
- **API docs:** http://localhost:8084/swagger-ui.html

## Endpoints

| Method | Path | Description |
|---|---|---|
| `POST` | `/notifications` | Create a notification |
| `GET` | `/notifications/unread` | List unread notifications |
| `POST` | `/notifications/{id}/read` | Mark a notification read |

## Run

```bash
mvn -B -f pom.xml clean verify
java -jar target/notification-service-0.1.0-SNAPSHOT.jar
```

The Kafka bootstrap address defaults to `localhost:9092` (`SPRING_KAFKA_BOOTSTRAP_SERVERS`).
