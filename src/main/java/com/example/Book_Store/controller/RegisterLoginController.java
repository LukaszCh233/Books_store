package com.example.Book_Store.controller;

import ch.qos.logback.classic.Logger;
import com.example.Book_Store.config.HelpJwt;
import com.example.Book_Store.config.JwtTokenServiceImpl;
import com.example.Book_Store.entities.Admin;
import com.example.Book_Store.entities.Customer;
import com.example.Book_Store.entities.CustomerLogin;
import com.example.Book_Store.exceptions.IncorrectPasswordException;
import com.example.Book_Store.service.CustomerLoginService;
import com.example.Book_Store.service.implementation.AdminServiceImpl;
import com.example.Book_Store.service.implementation.CustomerLoginServiceImpl;
import com.example.Book_Store.service.implementation.CustomerServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/access")
public class RegisterLoginController {
    private final HelpJwt helpJwt;
    private final Logger logger = (Logger) LoggerFactory.getLogger(RegisterLoginController.class);
    CustomerLoginService customerLoginService;
    CustomerServiceImpl customerService;
    AdminServiceImpl adminService;
    JwtTokenServiceImpl jwtTokenService;
    PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterLoginController(HelpJwt helpJwt, CustomerLoginServiceImpl customerLoginService, CustomerServiceImpl customerService, AdminServiceImpl adminService, JwtTokenServiceImpl jwtTokenService, PasswordEncoder passwordEncoder) {
        this.helpJwt = helpJwt;
        this.customerLoginService = customerLoginService;
        this.customerService = customerService;
        this.adminService = adminService;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/customerReg")
    ResponseEntity<?> registerCustomer(@RequestBody Customer customer) {
        Customer createCustomer = customerService.createCustomer(customer);
        CustomerDTO customerDTO = customerService.mapCustomerToCustomerDTO(createCustomer);
        logger.info("User registered successfully: {}", createCustomer.getCustomerLogin().getEmail());
        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }

    @PostMapping("/adminReg")
    ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {

        Admin createAdmin = adminService.createAdmin(admin);
        AdminDTO adminDTO = adminService.mapAdminToAdminDTO(createAdmin);
        logger.info("User registered successfully: {}", createAdmin.getEmail());
        return new ResponseEntity<>(adminDTO, HttpStatus.OK);
    }

    @PostMapping("/customerLog")
    ResponseEntity<?> loginCustomer(@RequestBody CustomerLogin customer) {
        Customer registerCustomer = customerService.findByEmail(customer.getEmail()).orElseThrow(()
                -> new EntityNotFoundException("Customer not exists"));

        if (!passwordEncoder.matches(customer.getPassword(), registerCustomer.getPassword())) {
            throw new IncorrectPasswordException("Incorrect email or password");
        }
        String jwtToken = helpJwt.generateToken(registerCustomer);
        logger.info("User logged in successfully: {}", registerCustomer.getCustomerLogin().getEmail());
        return ResponseEntity.ok(jwtToken);
    }

    @PostMapping("/adminLog")
    ResponseEntity<?> loginAdmin(@RequestBody Admin admin) {

        Admin registeredAdmin = adminService.findByEmail(admin.getEmail()).orElseThrow(()
                -> new EntityNotFoundException("Admin not exists"));

        if (!passwordEncoder.matches(admin.getPassword(), registeredAdmin.getPassword())) {
            throw new IncorrectPasswordException("Incorrect email or password");
        }
        String jwtToken = helpJwt.generateToken(registeredAdmin);
        logger.info("User logged in successfully: {}", registeredAdmin.getEmail());
        return ResponseEntity.ok(jwtToken);
    }
}
