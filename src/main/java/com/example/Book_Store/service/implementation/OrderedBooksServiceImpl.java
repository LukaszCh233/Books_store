package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.OrderedBooks;
import com.example.Book_Store.repository.OrderedBooksRepository;
import com.example.Book_Store.service.OrderedBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderedBooksServiceImpl implements OrderedBooksService {
    private final OrderedBooksRepository orderedBooksRepository;

    @Autowired
    public OrderedBooksServiceImpl(OrderedBooksRepository orderedBooksRepository) {
        this.orderedBooksRepository = orderedBooksRepository;
    }

    @Override
    public List<OrderedBooks> saveOrderedBooks(List<OrderedBooks> orderedBooksList) {
        return orderedBooksRepository.saveAll(orderedBooksList);
    }
}
