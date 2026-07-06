package ch.ledgerflow.settlement.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "account")
public record AccountProperties(String baseUrl) {
}
