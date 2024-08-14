package org.bdabos.model;

import org.bdabos.exceptions.BalanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class AccountTest {

    Account account;

    @Test
    @DisplayName("Deposit should increase the balance")
    void depositTest() {
        account = Account.builder().balance(BigDecimal.ZERO).build();

        account.deposit(BigDecimal.valueOf(100));
        assertEquals(BigDecimal.valueOf(100), account.getBalance());
    }

    @Test
    @DisplayName("Withdraw should decrease the balance")
    void withdrawTest() {
        account = Account.builder().balance(BigDecimal.valueOf(100)).build();

        account.withdraw(BigDecimal.valueOf(50));
        assertEquals(BigDecimal.valueOf(50), account.getBalance());
    }

    @Test
    @DisplayName("Withdraw should throw exception when balance is insufficient")
    void withdrawInsufficientBalanceTest() {
        account = Account.builder().balance(BigDecimal.valueOf(100)).build();

        assertThrows(BalanceException.class, () -> account.withdraw(BigDecimal.valueOf(150)));
    }
}
