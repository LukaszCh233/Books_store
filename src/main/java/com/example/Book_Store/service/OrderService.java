package com.example.Book_Store.service;

import com.example.Book_Store.controller.OrderDTO;
import com.example.Book_Store.entities.Order;

import java.util.List;

public interface OrderService {
    OrderDTO findOrderById(Integer id);
    List<OrderDTO> findAllOrders();
    boolean orderExistsById(Integer id);
    Order updateOrderStatus(Integer id);
}
