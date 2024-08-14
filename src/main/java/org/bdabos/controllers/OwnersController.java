package org.bdabos.controllers;

import lombok.RequiredArgsConstructor;
import org.bdabos.dtos.OwnerToAccountTransferDto;
import org.bdabos.services.TransferService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnersController {

    private final TransferService transferService;

    @PostMapping("{ownerId}/transfers")
    public ResponseEntity<String> transfer(@PathVariable Long ownerId, @Validated @RequestBody OwnerToAccountTransferDto ownerToAccountTransferDto) {
        Long accountId = ownerToAccountTransferDto.getAccountTo();
        Long ownerTo = ownerToAccountTransferDto.getOwnerTo();

        validateDestination(accountId, ownerTo);

        if (accountId != null) {
            transferService.ownerToAccountTransfer(ownerId, accountId, ownerToAccountTransferDto.getCurrency(), ownerToAccountTransferDto.getAmount());
        } else {
            transferService.ownerToOwnerTransfer(ownerId, ownerTo, ownerToAccountTransferDto.getCurrency(), ownerToAccountTransferDto.getAmount());
        }

        return ResponseEntity.ok("Transfer done");
    }

    private void validateDestination(Long accountId, Long ownerTo) {
        if (accountId == null && ownerTo == null) {
            throw new IllegalArgumentException("At least one destination must be provided. Either account or owner");
        }
        if (accountId != null && ownerTo != null) {
            throw new IllegalArgumentException("Only one destination must be provided. Either account or owner");
        }
    }
}
