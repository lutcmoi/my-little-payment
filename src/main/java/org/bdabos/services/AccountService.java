package org.bdabos.services;

import lombok.extern.slf4j.Slf4j;
import org.bdabos.exceptions.AccountNotFoundException;
import org.bdabos.exceptions.OwnerNotFoundException;
import org.bdabos.model.Account;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class AccountService {

    private final static String[] currencies = {"USD", "EUR", "GBP", "JPY", "CHF", "CAD", "AUD", "NZD", "CNY", "SEK"};

    private Map<Long, Account> accountsById = new HashMap<>();
    private Map<Long, Set<Long>> accountsIdsByOwnerId = new HashMap<>();

    public AccountService() {
        for (int i = 0; i < 10; i++) {
            create(1, currencies[i], 1000);
            create(2, currencies[i], 1000);
            create(3, currencies[i], 1000);
        }
    }

    private void create(long ownerId, String currency, double balance) {
        Set<Long> ownerAccountIds = accountsIdsByOwnerId.computeIfAbsent(ownerId, k -> new HashSet<>());

        Account account = Account.builder()
                .id(accountsById.size() + 1)
                .ownerId(ownerId)
                .currency(currency)
                .balance(BigDecimal.valueOf(balance))
                .build();

        ownerAccountIds.add(account.getId());
        accountsById.put(account.getId(), account);
    }

    public Account get(long accountId) {
        Account account = accountsById.get(accountId);
        if (account == null) {
            log.warn("Account with id=[{}] not found", accountId);
            throw new AccountNotFoundException("Account not found", accountId);
        }

        return account;
    }

    public Account get(long ownerId, String currency) {
        Set<Long> ownerAccountIds = accountsIdsByOwnerId.get(ownerId);
        if (ownerAccountIds == null) {
            log.warn("Owner with id=[{}] not found", ownerId);
            throw new OwnerNotFoundException("Owner not found", ownerId);
        }

        for (Long accountId : ownerAccountIds) {
            Account account = accountsById.get(accountId);
            if (account.getCurrency().equals(currency)) {
                return account;
            }
        }

        log.warn("Account with currency=[{}] not found for owner with id=[{}]", currency, ownerId);
        throw new AccountNotFoundException("Account not found for the specified currency");
    }

}
