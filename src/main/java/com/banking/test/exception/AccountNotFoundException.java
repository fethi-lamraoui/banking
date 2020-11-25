package com.banking.test.exception;

public class AccountNotFoundException extends RuntimeException {

    public static final String ACCOUNT_NOT_FOUND_EXCEPTION = "Account not found...";

    public AccountNotFoundException() {
        this(ACCOUNT_NOT_FOUND_EXCEPTION);
    }

    public AccountNotFoundException(String message) {
        super(message);
    }

}
