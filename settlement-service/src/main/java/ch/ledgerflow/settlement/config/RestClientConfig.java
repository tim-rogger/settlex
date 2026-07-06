package ch.ledgerflow.settlement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient accountRestClient(AccountProperties accountProperties) {
        return RestClient.builder()
                .baseUrl(accountProperties.baseUrl())
                .build();
    }
}
