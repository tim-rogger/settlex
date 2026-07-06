package ch.ledgerflow.account.web;

import ch.ledgerflow.account.domain.Account;
import ch.ledgerflow.account.error.AccountNotFoundException;
import ch.ledgerflow.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    void createsAccountAndReturns201() throws Exception {
        given(accountService.createAccount(any(), any()))
                .willReturn(new Account("Alice", "CHF"));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"ownerName\":\"Alice\",\"currency\":\"CHF\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ownerName").value("Alice"))
                .andExpect(jsonPath("$.currency").value("CHF"));
    }

    @Test
    void returns404WhenAccountNotFound() throws Exception {
        given(accountService.getAccount(any()))
                .willThrow(new AccountNotFoundException(99L));

        mockMvc.perform(get("/accounts/99"))
                .andExpect(status().isNotFound());
    }
}
