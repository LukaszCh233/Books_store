package com.example.Book_Store.controller;

import com.example.Book_Store.entities.Customer;
import com.example.Book_Store.entities.CustomerLogin;
import com.example.Book_Store.service.*;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;

@RestController
@RequestMapping("/")
public class CustomerController {
    private final BookService bookService;
    private final CategoryService categoryService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final CustomerLoginService customerLoginService;

    @Autowired
    public CustomerController(BookService bookService, CategoryService categoryService,
                              CustomerService customerService, OrderService orderService, CustomerLoginService customerLoginService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.customerLoginService = customerLoginService;
    }
    @PermitAll
    @PostMapping("/customer/register")
    public ResponseEntity<String> registerCustomer(@RequestBody Customer customer) {
        if (customer.getCustomerLogin() == null) {
            return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
        }
        if (customerLoginService.existsByEmail(customer.getCustomerLogin().getEmail())) {
            return new ResponseEntity<>("this email already exists ", HttpStatus.CONFLICT);
        }
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(customer.getCustomerLogin().getPassword());
        customer.getCustomerLogin().setPassword(hashedPassword);

        customerService.createCustomer(customer);

        return new ResponseEntity<>("Correct register", HttpStatus.CREATED);

    }

    @PostMapping("/customer/login")
    public ResponseEntity<String> loginCustomer(@RequestBody CustomerLogin customerLogin) {
        CustomerLogin storedCustomerLogin = customerLoginService.findPasswordByEmail(customerLogin.getEmail());
        if (storedCustomerLogin != null) {
            String hashedPasswordFromDatabase = storedCustomerLogin.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(customerLogin.getPassword(), hashedPasswordFromDatabase)) {
                byte[] keyBytes = "SECRET_KEY".getBytes();
                SecretKey key = Keys.hmacShaKeyFor(keyBytes);

                String token = Jwts.builder()
                        .setSubject(customerLogin.getEmail())
                        .signWith(key)
                        .compact();
                return new ResponseEntity<>("Login successful", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }


}
