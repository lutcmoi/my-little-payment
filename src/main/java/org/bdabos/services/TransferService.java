package org.bdabos.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bdabos.model.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransferService {

    private final AccountService accountService;
    private final ExchangeService exchangeService;

    public void accountsTransfer(long accountIdFrom, long accountIdTo, BigDecimal amount) {
        if (accountIdFrom == accountIdTo) {
            log.warn("Cannot transfer to the same account with id=[{}]", accountIdFrom);
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }

        // retrieve accounts
        Account from = accountService.get(accountIdFrom);
        Account to = accountService.get(accountIdTo);

        // Get the amount in the currency of the destination account
        BigDecimal finalAmount = exchangeService.getFinalAmount(from, to, amount);

        from.withdraw(amount);
        to.deposit(finalAmount);

        log.info("Transfer done");
    }

    public void ownerToAccountTransfer(Long ownerIdFrom, Long accountIdTo, String currency, BigDecimal amount) {
        // retrieve owner account for currency
        Account from = accountService.get(ownerIdFrom, currency);

        accountsTransfer(from.getId(), accountIdTo, amount);
    }

    public void ownerToOwnerTransfer(Long ownerIdFrom, Long ownerIdTo, String currency, BigDecimal amount) {
        // retrieve owners accounts for currency
        Account from = accountService.get(ownerIdFrom, currency);
        Account to = accountService.get(ownerIdTo, currency);

        accountsTransfer(from.getId(), to.getId(), amount);
    }

}
