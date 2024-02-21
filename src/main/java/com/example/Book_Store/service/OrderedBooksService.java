package com.example.Book_Store.service;

import com.example.Book_Store.entities.OrderedBooks;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderedBooksService {
    List<OrderedBooks> saveOrderedBooks(List<OrderedBooks> orderedBooksList);
}
