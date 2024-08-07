package com.example.Book_Store.order.repository;

import com.example.Book_Store.enums.Status;
import com.example.Book_Store.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(Status status);
}
