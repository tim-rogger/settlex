package ch.ledgerflow.settlement.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class AccountClient {

    private final RestClient accountRestClient;

    public AccountClient(RestClient accountRestClient) {
        this.accountRestClient = accountRestClient;
    }

    public TransferResponse transfer(TransferRequest request) {
        return accountRestClient.post()
                .uri("/transfers")
                .body(request)
                .retrieve()
                .body(TransferResponse.class);
    }
}
