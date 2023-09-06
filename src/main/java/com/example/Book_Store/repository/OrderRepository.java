package com.example.Book_Store.repository;

import com.example.Book_Store.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findOrderById(Long id);
}
