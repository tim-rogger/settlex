package ch.settlex.account.web;

import ch.settlex.account.domain.Account;
import ch.settlex.account.service.AccountService;
import ch.settlex.account.web.dto.AccountResponse;
import ch.settlex.account.web.dto.CreateAccountRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public Account create(@RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request.OwnerName(), request.currency());
    }

    @GetMapping("/{id}")
    public AccountResponse get(@PathVariable Long id) {
        return AccountResponse.from(accountService.getAccount(id));
    }
}
