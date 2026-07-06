# gateway

The single entry point for LedgerFlow, built on **Spring Cloud Gateway**. It routes incoming requests to the four backend services and applies CORS for the (planned) React frontend at `http://localhost:5173`.

- **Port:** 8080

## Routes

| Path prefix | Target service | Port |
|---|---|---|
| `/accounts/**` | account-service | 8081 |
| `/transfers/**` | account-service | 8081 |
| `/orders/**` | order-service | 8082 |
| `/settlements/**` | settlement-service | 8083 |
| `/notifications/**` | notification-service | 8084 |

With the full stack running, every service API is reachable through `http://localhost:8080`.

## Run

```bash
mvn -B -f pom.xml clean verify
java -jar target/gateway-0.1.0-SNAPSHOT.jar
```

Routing assumes the backend services are reachable at their default localhost ports; adjust the URIs in `application.yml` for other environments.
