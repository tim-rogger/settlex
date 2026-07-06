package ch.ledgerflow.account.web;

import ch.ledgerflow.account.service.AccountService;
import ch.ledgerflow.account.web.dto.AccountResponse;
import ch.ledgerflow.account.web.dto.CreateAccountRequest;
import ch.ledgerflow.account.web.dto.DepositRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse create(@Valid @RequestBody CreateAccountRequest request) {
        return AccountResponse.from(accountService.createAccount(request.ownerName(), request.currency()));
    }

    @GetMapping
    public List<AccountResponse> list() {
        return accountService.listAccounts().stream().map(AccountResponse::from).toList();
    }

    @GetMapping("/{id}")
    public AccountResponse get(@PathVariable Long id) {
        return AccountResponse.from(accountService.getAccount(id));
    }

    @PostMapping("/{id}/deposit")
    public AccountResponse deposit(@PathVariable Long id, @Valid @RequestBody DepositRequest request) {
        return AccountResponse.from(accountService.deposit(id, request.amount()));
    }
}
