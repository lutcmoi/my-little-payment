package org.bdabos.controllers;

import lombok.RequiredArgsConstructor;
import org.bdabos.dtos.AccountsTransferDto;
import org.bdabos.services.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountsController {

    private final TransferService transferService;

    @PostMapping("{accountId}/transfer")
    public ResponseEntity<String> transfer(@PathVariable Long accountId,  @Validated @RequestBody AccountsTransferDto accountsTransferDto) {
        transferService.accountsTransfer(accountId, accountsTransferDto.getTo(), accountsTransferDto.getAmount());
        return ResponseEntity.ok("Transfer done");
    }
}
