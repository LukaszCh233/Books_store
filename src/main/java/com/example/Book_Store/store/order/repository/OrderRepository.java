package com.example.Book_Store.store.order.repository;

import com.example.Book_Store.store.Status;
import com.example.Book_Store.store.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(Status status);
}
