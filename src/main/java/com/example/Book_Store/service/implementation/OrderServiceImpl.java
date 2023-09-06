package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.Order;
import com.example.Book_Store.repository.OrderRepository;
import com.example.Book_Store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order findOrderById(Long id) {
        return orderRepository.findOrderById(id);
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order sendOrder(Order order) {
        return orderRepository.save(order);
    }
}
