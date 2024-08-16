package org.bdabos.controllers;

import lombok.extern.slf4j.Slf4j;
import org.bdabos.exceptions.AccountNotFoundException;
import org.bdabos.exceptions.BalanceException;
import org.bdabos.exceptions.ExchangeRateUnavailableException;
import org.bdabos.exceptions.LittlePaymentResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.springframework.web.util.BindErrorUtils;

@Slf4j
@RestControllerAdvice
public class ControllerErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleException(MethodArgumentNotValidException e) {
        log.warn("Impossible to handle request", e);
        String sb = "Invalid request: \n" + BindErrorUtils.resolveAndJoin(e.getFieldErrors());
        return ResponseEntity.badRequest().body(sb);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(LittlePaymentResourceNotFoundException.class)
    public ResponseEntity<String> handleException(AccountNotFoundException e) {
        if (e.getResourceId() == null) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.badRequest().body("Resource not found with id=" + e.getResourceId());
    }

    @ExceptionHandler(BalanceException.class)
    public ResponseEntity<String> handleException(BalanceException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(ExchangeRateUnavailableException.class)
    public ResponseEntity<String> handleException(ExchangeRateUnavailableException e) {
        log.warn("Exchange rate unavailable", e);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(e.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handleException(NoResourceFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("An unexpected error occurred", e);
        return ResponseEntity.internalServerError().body("An error occurred, we will investigate shortly");
    }
}
