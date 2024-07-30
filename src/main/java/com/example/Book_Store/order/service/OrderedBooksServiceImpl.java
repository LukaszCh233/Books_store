package com.example.Book_Store.order.service;

import com.example.Book_Store.order.entity.OrderedBooks;
import com.example.Book_Store.order.repository.OrderedBooksRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderedBooksServiceImpl {
    private final OrderedBooksRepository orderedBooksRepository;

    public OrderedBooksServiceImpl(OrderedBooksRepository orderedBooksRepository) {
        this.orderedBooksRepository = orderedBooksRepository;
    }

    public List<OrderedBooks> saveOrderedBooks(List<OrderedBooks> orderedBooksList) {
        return orderedBooksRepository.saveAll(orderedBooksList);
    }
}
