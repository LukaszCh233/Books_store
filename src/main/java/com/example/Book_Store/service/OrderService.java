package com.example.Book_Store.service;

import com.example.Book_Store.controller.OrderDTO;
import com.example.Book_Store.entities.Order;

import java.security.Principal;
import java.util.List;

public interface OrderService {
    OrderDTO findOrderById(Integer id);

    List<OrderDTO> findAllOrders();

    Order updateOrderStatus(Integer id);

    Order saveOrder(Order order, Principal principal);
}
