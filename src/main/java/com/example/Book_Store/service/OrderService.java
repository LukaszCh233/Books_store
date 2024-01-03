package com.example.Book_Store.service;

import com.example.Book_Store.entities.Book;
import com.example.Book_Store.entities.Order;
import org.aspectj.weaver.ast.Or;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order findOrderById(Integer id);
    List<Order> findAllOrders();

    boolean orderExistsById(Integer id);
    Order saveOrder(Order order);
}
