package com.example.Book_Store.exceptions;

public class OperationNotAllowedException extends RuntimeException {

    public OperationNotAllowedException() {
        super();
    }

    public OperationNotAllowedException(String message) {
        super(message);
    }

    public OperationNotAllowedException(String message, Throwable cause) {
        super(message, cause);
    }
}
