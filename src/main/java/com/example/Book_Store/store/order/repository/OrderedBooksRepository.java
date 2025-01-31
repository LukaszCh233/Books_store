package com.example.Book_Store.store.order.repository;

import com.example.Book_Store.store.order.entity.OrderedBooks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedBooksRepository extends JpaRepository<OrderedBooks, Long> {
}
