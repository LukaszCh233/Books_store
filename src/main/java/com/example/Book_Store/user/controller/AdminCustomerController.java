package com.example.Book_Store.user.controller;

import com.example.Book_Store.user.dto.CustomerDTO;
import com.example.Book_Store.user.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminCustomerController {
    private final CustomerService customerService;

    public AdminCustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> displayCustomers() {
        List<CustomerDTO> customerDTOS = customerService.findAllCustomers();

        return ResponseEntity.ok(customerDTOS);
    }
}
