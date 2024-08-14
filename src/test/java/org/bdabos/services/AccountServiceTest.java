package org.bdabos.services;

import org.bdabos.exceptions.AccountNotFoundException;
import org.bdabos.exceptions.OwnerNotFoundException;
import org.bdabos.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private AccountService accountService;

    @Test
    @DisplayName("Exception should be thrown when trying retrieve an unknown account")
    void exceptionThrownWhenUnknownAccount() {
        accountService = new AccountService();
        assertThrows(AccountNotFoundException.class, () -> accountService.get(-1));
    }

    @Test
    @DisplayName("Account should be retrieved when it exists")
    void accountShouldBeRetrieved() {
        accountService = new AccountService();

        Account account = accountService.get(1);
        assertNotNull(account);
        assertEquals(1, account.getId());
    }

    @Test
    @DisplayName("Exception should be thrown when trying retrieve an account from an unknown owner")
    void exceptionThrownWhenUnknownOwner() {
        accountService = new AccountService();
        assertThrows(OwnerNotFoundException.class, () -> accountService.get(-1, "USD"));
    }

    @Test
    @DisplayName("Exception should be thrown when trying retrieve an unknown currency account from an owner")
    void exceptionThrownWhenUnknownAccountFormCurrency() {
        accountService = new AccountService();
        assertThrows(AccountNotFoundException.class, () -> accountService.get(1, "TOT"));
    }

    @Test
    @DisplayName("Owner account should be retrieved when it exists")
    void ownerAccountShouldBeRetreived() {
        accountService = new AccountService();

        Account account = accountService.get(1, "AUD");
        assertNotNull(account);
        assertEquals("AUD", account.getCurrency());
    }
}
