package com.exemplo.modelo.exception;

public class EmailJaExisteException extends RuntimeException {
    public EmailJaExisteException(String message) {
        super(message);
    }
}
