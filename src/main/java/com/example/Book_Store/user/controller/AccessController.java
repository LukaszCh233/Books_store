package com.example.Book_Store.user.controller;

import com.example.Book_Store.user.dto.AdminDTO;
import com.example.Book_Store.user.dto.CustomerDTO;
import com.example.Book_Store.user.entity.Admin;
import com.example.Book_Store.user.entity.Customer;
import com.example.Book_Store.user.entity.LoginRequest;
import com.example.Book_Store.user.service.AdminService;
import com.example.Book_Store.user.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/access")
public class AccessController {

    private final CustomerService customerService;
    private final AdminService adminService;

    public AccessController(CustomerService customerService, AdminService adminService) {
        this.customerService = customerService;
        this.adminService = adminService;
    }

    @PostMapping("/customer-register")
    ResponseEntity<CustomerDTO> registerCustomer(@Valid @RequestBody Customer customer) {
        CustomerDTO customerDTO = customerService.createCustomer(customer);

        return ResponseEntity.ok(customerDTO);
    }

    @PostMapping("/customer-login")
    ResponseEntity<?> loginCustomer(@RequestBody LoginRequest customer) {
        String jwtToken = customerService.customerAuthorization(customer);

        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/admin-register")
    ResponseEntity<AdminDTO> registerAdmin(@Valid @RequestBody Admin admin) {
        AdminDTO createAdmin = adminService.createAdmin(admin);

        return new ResponseEntity<>(createAdmin, HttpStatus.OK);
    }

    @PostMapping("/admin-login")
    ResponseEntity<?> loginAdmin(@RequestBody LoginRequest admin) {
        String jwtToken = adminService.adminAuthorization(admin);

        return ResponseEntity.ok(jwtToken);
    }
}



