package com.banking.test.exception;

public class UserExistException extends RuntimeException {

    public static final String USER_EXIST_EXCEPTION = "User exist...";

    public UserExistException() {
        this(USER_EXIST_EXCEPTION);
    }

    public UserExistException(String message) {
        super(message);
    }

}
