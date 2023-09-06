package com.example.Book_Store.service;

import com.example.Book_Store.entities.Book;
import com.example.Book_Store.entities.Order;
import org.aspectj.weaver.ast.Or;

import java.util.List;

public interface OrderService {
    Order findOrderById(Long id);
    List<Order> findAllOrders();
    Order sendOrder(Order order);
}
