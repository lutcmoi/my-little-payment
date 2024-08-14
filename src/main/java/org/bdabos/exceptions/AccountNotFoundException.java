package org.bdabos.exceptions;

public class AccountNotFoundException extends LittlePaymentResourceNotFoundException {
    public AccountNotFoundException(String message) {
        super(message);
    }

    public AccountNotFoundException(String message, Long resourceId) {
        super(message, resourceId);
    }
}
