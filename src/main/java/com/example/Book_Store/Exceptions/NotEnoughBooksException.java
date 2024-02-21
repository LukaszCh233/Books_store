package com.example.Book_Store.exceptions;

public class NotEnoughBooksException extends RuntimeException {

    public NotEnoughBooksException() {
        super();
    }

    public NotEnoughBooksException(String message) {
        super(message);
    }

    public NotEnoughBooksException(String message, Throwable cause) {
        super(message, cause);
    }
}
