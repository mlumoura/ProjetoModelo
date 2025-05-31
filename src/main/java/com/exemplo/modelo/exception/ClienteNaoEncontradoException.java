package com.exemplo.modelo.exception;

public class ClienteNaoEncontradoException extends RuntimeException {
    public ClienteNaoEncontradoException(String message) {
        super(message);
    }
}
