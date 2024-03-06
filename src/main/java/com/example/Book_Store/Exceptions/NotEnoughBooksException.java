package com.example.Book_Store.exceptions;

public class NotEnoughBooksException extends RuntimeException {

    public NotEnoughBooksException(String message) {
        super(message);
    }

}
