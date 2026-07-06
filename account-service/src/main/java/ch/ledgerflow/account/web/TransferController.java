package ch.ledgerflow.account.web;

import ch.ledgerflow.account.service.TransferService;
import ch.ledgerflow.account.web.dto.TransferRequest;
import ch.ledgerflow.account.web.dto.TransferResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    public TransferResponse transfer(@Valid @RequestBody TransferRequest request) {
        return transferService.transfer(request);
    }
}
