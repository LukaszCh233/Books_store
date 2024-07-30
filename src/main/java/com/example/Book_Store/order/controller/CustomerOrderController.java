package com.example.Book_Store.order.controller;

import com.example.Book_Store.order.entity.Order;
import com.example.Book_Store.order.service.OrderServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/customer")
public class CustomerOrderController {
    private final OrderServiceImpl orderService;

    public CustomerOrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/order")
    public ResponseEntity<?> orderBooks(Order order, Principal principal) {

        orderService.saveOrder(order, principal);
        return ResponseEntity.ok("Order is completed");
    }
}
