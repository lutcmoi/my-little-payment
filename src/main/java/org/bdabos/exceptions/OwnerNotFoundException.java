package org.bdabos.exceptions;

public class OwnerNotFoundException extends LittlePaymentResourceNotFoundException {
    public OwnerNotFoundException(String message) {
        super(message);
    }

    public OwnerNotFoundException(String message, Long resourceId) {
        super(message, resourceId);
    }
}
