package com.example.Book_Store.account.admin;

import com.example.Book_Store.account.customer.CustomerDTO;
import com.example.Book_Store.account.customer.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final CustomerService customerService;

    public AdminController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> displayCustomers() {
        List<CustomerDTO> customerDTOS = customerService.findAllCustomers();

        return ResponseEntity.ok(customerDTOS);
    }
}
