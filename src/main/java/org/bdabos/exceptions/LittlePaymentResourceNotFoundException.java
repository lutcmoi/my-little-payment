package org.bdabos.exceptions;

import lombok.Getter;

@Getter
public class LittlePaymentResourceNotFoundException extends RuntimeException {

    private final Long resourceId;

    public LittlePaymentResourceNotFoundException(String message) {
        this(message, null);
    }

    public LittlePaymentResourceNotFoundException(String message, Long resourceId) {
        super(message);
        this.resourceId = resourceId;
    }
}
