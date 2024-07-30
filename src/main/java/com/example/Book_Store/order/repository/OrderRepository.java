package com.example.Book_Store.order.repository;

import com.example.Book_Store.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
