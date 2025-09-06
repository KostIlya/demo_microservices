package ru.t1.demo_t1.exception;

public class NoEntityException extends RuntimeException {
    public NoEntityException(String message) {
        super(message);
    }
}
