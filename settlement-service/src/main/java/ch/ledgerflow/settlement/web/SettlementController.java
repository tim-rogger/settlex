package ch.ledgerflow.settlement.web;

import ch.ledgerflow.settlement.service.SettlementService;
import ch.ledgerflow.settlement.web.dto.SettleRequest;
import ch.ledgerflow.settlement.web.dto.SettlementResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/settlements")
public class SettlementController {

    private final SettlementService settlementService;

    public SettlementController(SettlementService settlementService) {
        this.settlementService = settlementService;
    }

    @PostMapping
    public SettlementResponse settle(@Valid @RequestBody SettleRequest request) {
        return SettlementResponse.from(settlementService.settle(
                request.idempotencyKey(), request.orderId(), request.amount(), request.currency()));
    }

    @GetMapping("/{id}")
    public SettlementResponse get(@PathVariable Long id) {
        return SettlementResponse.from(settlementService.getSettlement(id));
    }
}
