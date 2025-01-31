package com.example.Book_Store.store.book.dto;

import com.example.Book_Store.store.Status;

public record BookDTO(String title, String author, Double price, Long quantity, String categoryName, Status status) {
}
