package ru.nshi.error;

public class MessageException extends RuntimeException {
    public MessageException(String message) {
        super(message);
    }
}
