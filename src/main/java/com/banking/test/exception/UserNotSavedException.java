package com.banking.test.exception;

public class UserNotSavedException extends RuntimeException {

    public static final String USER_NOT_SAVED_EXCEPTION = "User not saved...";

    public UserNotSavedException() {
        this(USER_NOT_SAVED_EXCEPTION);
    }

    public UserNotSavedException(String message) {
        super(message);
    }

}
