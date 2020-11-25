package com.banking.test.exception;

public class TransactionException extends RuntimeException {

    public static final String TRANSACTION_EXCEPTION = "Error whith the transaction...";
    public static final String WITHDRAW_EXCEPTION = "Error when withdraw...";

    public TransactionException() {
        this(TRANSACTION_EXCEPTION);
    }

    public TransactionException(String message) {
        super(message);
    }

}
