package com.springbootdev.examples.security.basic.exception;

public class InvalidUserIdException extends ValidationException {

    public InvalidUserIdException(String message) {
        super(message);
    }
}
