package org.bdabos.model;

import lombok.Builder;
import lombok.Data;
import lombok.Synchronized;
import org.bdabos.exceptions.BalanceException;

import java.math.BigDecimal;

@Builder
@Data
public class Account {
    private long id;
    private long ownerId;

    @Builder.Default
    private String currency = "EUR";

    @Builder.Default
    private volatile BigDecimal balance = new BigDecimal(0);

    @Synchronized
    public void deposit(BigDecimal amount) {
        balance = balance.add(amount);
    }

    @Synchronized
    public void withdraw(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) {
            throw new BalanceException("Account with id=[" + id + "] has insufficient funds=[" + balance + "]");
        }

        balance = balance.subtract(amount);
    }
}
