package org.bdabos.services;

import org.bdabos.model.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    private final AccountService accountService = mock(AccountService.class);
    private final ExchangeService exchangeService = mock(ExchangeService.class);

    private final TransferService transferService = new TransferService(accountService, exchangeService);

    @Test
    @DisplayName("Transfer should fail when trying to transfer to the same account")
    void sameAccountTransferTest() {
        assertThrows(IllegalArgumentException.class, () -> transferService.accountsTransfer(1, 1, BigDecimal.valueOf(100)));
    }

    @Test
    @DisplayName("Transfer should succeed")
    void transferSuccess() {
        Account from = mock(Account.class);
        Account to = mock(Account.class);

        when(accountService.get(1)).thenReturn(from);
        when(accountService.get(2)).thenReturn(to);

        when(exchangeService.getFinalAmount(any(Account.class), any(Account.class), any(BigDecimal.class))).thenReturn(BigDecimal.valueOf(150));

        transferService.accountsTransfer(1, 2, BigDecimal.valueOf(100));
        verify(from).withdraw(BigDecimal.valueOf(100).setScale(2, RoundingMode.HALF_UP));
        verify(to).deposit(BigDecimal.valueOf(150).setScale(2, RoundingMode.HALF_UP));
    }
}
