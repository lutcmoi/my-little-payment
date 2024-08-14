package org.bdabos.model;

import org.bdabos.exceptions.BalanceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    @RepeatedTest(1000)
    @DisplayName("Concurrent withdraw")
    void concurrentWithdrawTest() throws InterruptedException {
        account = Account.builder().balance(BigDecimal.valueOf(10000)).build();

        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            executor.execute(() -> account.withdraw(BigDecimal.valueOf(100)));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        assertEquals(BigDecimal.ZERO, account.getBalance());
    }

    @RepeatedTest(1000)
    @DisplayName("Concurrent deposit")
    void concurrentDepositTest() throws InterruptedException {
        account = Account.builder().balance(BigDecimal.ZERO).build();

        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            executor.execute(() -> account.deposit(BigDecimal.valueOf(100)));
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        assertEquals(BigDecimal.valueOf(10000), account.getBalance());
    }

    @RepeatedTest(1000)
    @DisplayName("Concurrent deposit and withdraw")
    void concurrentDepositWithdrawTest() throws InterruptedException {
        account = Account.builder().balance(BigDecimal.valueOf(5000)).build();

        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            executor.execute(() -> {
                account.deposit(BigDecimal.valueOf(100));
            });
            executor.execute(() -> {
                account.withdraw(BigDecimal.valueOf(50));
            });
        }

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        assertEquals(BigDecimal.valueOf(10000), account.getBalance());
    }
}
