package com.example.Book_Store.store.order.controller;

import com.example.Book_Store.store.order.dto.OrderDTO;
import com.example.Book_Store.store.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminOrderController {
    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> displayOrders() {
        List<OrderDTO> orders = orderService.findAllOrders();

        return ResponseEntity.ok(orders);
    }

    @GetMapping
    private ResponseEntity<List<OrderDTO>> displaySentOrders() {
        List<OrderDTO> orders = orderService.findAllSentOrders();

        return ResponseEntity.ok(orders);
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderDTO> displayOrder(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.findOrderById(id);

        return ResponseEntity.ok(orderDTO);
    }

    @PutMapping("/order-send/{id}")
    public ResponseEntity<String> sendOrder(@PathVariable Long id) {
        orderService.updateOrderStatus(id);

        return ResponseEntity.ok("Order has been sent");
    }
}
