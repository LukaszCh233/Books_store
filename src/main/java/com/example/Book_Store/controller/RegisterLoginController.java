package com.example.Book_Store.controller;

import ch.qos.logback.classic.Logger;
import com.example.Book_Store.config.HelpJwt;
import com.example.Book_Store.config.JwtTokenServiceImpl;
import com.example.Book_Store.entities.Admin;
import com.example.Book_Store.entities.Customer;
import com.example.Book_Store.entities.CustomerLogin;
import com.example.Book_Store.service.AdminService;
import com.example.Book_Store.service.CustomerLoginService;
import com.example.Book_Store.service.CustomerService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/access")
public class RegisterLoginController {
    private final HelpJwt helpJwt;
    private final Logger logger = (Logger) LoggerFactory.getLogger(RegisterLoginController.class);
    CustomerLoginService customerLoginService;
    CustomerService customerService;
    AdminService adminService;
    JwtTokenServiceImpl jwtTokenService;
    PasswordEncoder passwordEncoder;

    @Autowired
    public RegisterLoginController(HelpJwt helpJwt, CustomerLoginService customerLoginService, CustomerService customerService, AdminService adminService, JwtTokenServiceImpl jwtTokenService, PasswordEncoder passwordEncoder) {
        this.helpJwt = helpJwt;
        this.customerLoginService = customerLoginService;
        this.customerService = customerService;
        this.adminService = adminService;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/customerReg")
    ResponseEntity<?> registerCustomer(@RequestBody Customer customer) {
        boolean checkEmail = customerLoginService.existsByEmail(customer.getCustomerLogin().getEmail());
        if (checkEmail) {
            logger.warn("Email already exists: {}", customer.getCustomerLogin().getEmail());
            return new ResponseEntity<>("Email exists", HttpStatus.BAD_REQUEST);
        } else {

            Customer create = customerService.createCustomer(customer);
            logger.info("User registered successfully: {}", create.getCustomerLogin().getEmail());
            return new ResponseEntity<>(create, HttpStatus.OK);
        }
    }

    @PostMapping("/adminReg")
    ResponseEntity<?> registerAdmin(@RequestBody Admin admin) {
        try {


            boolean checkEmail = adminService.existsByEmail(admin.getEmail());
            if (checkEmail) {
                logger.warn("Email already exists: {}", admin.getEmail());
                return new ResponseEntity<>("Email exists", HttpStatus.BAD_REQUEST);
            } else {

                Admin create = adminService.createAdmin(admin);
                logger.info("User registered successfully: {}", create.getEmail());
                return new ResponseEntity<>(create, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    @PostMapping("/customerLog")
    ResponseEntity<?> loginCustomer(@RequestBody CustomerLogin customer) {

        Optional<Customer> storedUserOptional = customerService.findByEmail(customer.getEmail());

        if (storedUserOptional.isPresent()) {

            Customer storedUser = storedUserOptional.get();
            if (passwordEncoder.matches(customer.getPassword(), storedUser.getPassword())) {
                logger.info("User logged in successfully: {}", storedUser.getCustomerLogin().getEmail());
                String jwtToken = helpJwt.generateToken(storedUser);

                return new ResponseEntity<>(jwtToken, HttpStatus.OK);
            } else {
                logger.warn("Incorrect password for user: {}", storedUser.getCustomerLogin().getEmail());
                return new ResponseEntity<>("Incorrect password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            logger.warn("User not found for email: {}", customer.getEmail());
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/adminLog")
    ResponseEntity<?> loginAdmin(@RequestBody Admin admin) {

        Optional<Admin> storedUserOptional = adminService.findByEmail(admin.getEmail());

        if (storedUserOptional.isPresent()) {

            Admin storedUser = storedUserOptional.get();
            if (passwordEncoder.matches(admin.getPassword(), storedUser.getPassword())) {
                logger.info("User logged in successfully: {}", storedUser.getEmail());
                String jwtToken = helpJwt.generateToken(storedUser);

                return new ResponseEntity<>(jwtToken, HttpStatus.OK);
            } else {
                logger.warn("Incorrect password for user: {}", storedUser.getEmail());
                return new ResponseEntity<>("Incorrect password", HttpStatus.UNAUTHORIZED);
            }
        } else {
            logger.warn("User not found for email: {}", admin.getEmail());
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}
