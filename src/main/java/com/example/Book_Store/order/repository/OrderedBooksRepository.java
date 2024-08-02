package com.example.Book_Store.order.repository;

import com.example.Book_Store.order.entity.OrderedBooks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedBooksRepository extends JpaRepository<OrderedBooks, Long> {
}
