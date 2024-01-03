package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.Order;
import com.example.Book_Store.repository.OrderRepository;
import com.example.Book_Store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;


    }

    @Override
    public Order findOrderById(Integer id) {
        return orderRepository.findOrderById(id);
    }

    @Override
    public List<Order> findAllOrders() {
        return orderRepository.findAll();
    }



    @Override
    public boolean orderExistsById(Integer id) {
        return orderRepository.existsById(id);
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }
}
