package com.example.Book_Store.service.implementation;

import com.example.Book_Store.controller.OrderDTO;
import com.example.Book_Store.entities.Order;
import com.example.Book_Store.entities.Status;
import com.example.Book_Store.repository.OrderRepository;
import com.example.Book_Store.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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
    public OrderDTO findOrderById(Integer id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return mapOrderToOrderDTO(order);
    }

    public OrderDTO mapOrderToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setCustomer(order.getCustomer());
        orderDTO.setOrderData(order.getOrderData());
        orderDTO.setPrice(order.getPrice());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setOrderedBooks(order.getOrderedBooks());
        return orderDTO;
    }

    @Override
    public List<OrderDTO> findAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("Orders list is empty");
        }

        return orders.stream()
                .map(this::mapOrderToOrderDTO)
                .toList();
    }

    @Override
    public boolean orderExistsById(Integer id) {
        return orderRepository.existsById(id);
    }

    @Override
    public Order updateOrderStatus(Integer id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getStatus() == Status.SENT) {
            throw new ResourceNotFoundException("Order with this ID has already been sent");
        }
        order.setStatus(Status.SENT);
        return orderRepository.save(order);
    }
}
