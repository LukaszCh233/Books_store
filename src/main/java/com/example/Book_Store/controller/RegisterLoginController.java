package com.example.Book_Store.controller;

import ch.qos.logback.classic.Logger;
import com.example.Book_Store.entities.Customer;
import com.example.Book_Store.entities.CustomerLogin;
import com.example.Book_Store.service.CustomerLoginService;
import com.example.Book_Store.service.CustomerService;
import com.example.Book_Store.service.implementation.JwtTokenServiceImpl;
import com.example.Book_Store.service.implementation.PasswordEncoderServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.security.PermitAll;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/customer")
public class RegisterLoginController {
    CustomerLoginService customerLoginService;
    CustomerService customerService;
    JwtTokenServiceImpl jwtTokenService;
    PasswordEncoderServiceImpl passwordEncoderService;
    private final Logger logger = (Logger) LoggerFactory.getLogger(RegisterLoginController.class);
@Autowired
    public RegisterLoginController(CustomerLoginService customerLoginService, CustomerService customerService, JwtTokenServiceImpl jwtTokenService, PasswordEncoderServiceImpl passwordEncoderService) {
        this.customerLoginService = customerLoginService;
        this.customerService = customerService;
        this.jwtTokenService = jwtTokenService;
        this.passwordEncoderService = passwordEncoderService;
    }

    @PermitAll
    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@RequestBody Customer customer) {
        if (customer.getCustomerLogin() == null) {
            return new ResponseEntity<>("Bad request", HttpStatus.BAD_REQUEST);
        }
        if (customerLoginService.existsByEmail(customer.getCustomerLogin().getEmail())) {
            return new ResponseEntity<>("this email already exists ", HttpStatus.CONFLICT);
        }

        String hashedPassword = passwordEncoderService.codingPassword(customer.getCustomerLogin().getPassword());
        customer.getCustomerLogin().setPassword(hashedPassword);

        customerService.createCustomer(customer);

        return new ResponseEntity<>("Correct register", HttpStatus.CREATED);

    }

    @PermitAll
    @PostMapping("/login")
    public ResponseEntity<String> loginCustomer(@RequestBody CustomerLogin customerLogin) {
        CustomerLogin storedCustomerLogin = customerLoginService.findPasswordByEmail(customerLogin.getEmail());
        if (storedCustomerLogin != null) {


            String hashedPasswordFromDatabase = storedCustomerLogin.getPassword();
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(customerLogin.getPassword(), hashedPasswordFromDatabase)) {

                String token = jwtTokenService.generateJwtToken(customerLogin);

                return new ResponseEntity<>(token, HttpStatus.OK);


            } else {
                logger.info("Attempting to log in customer with email: {}", customerLogin.getEmail());

                return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
            }
        } else {
            logger.error("Error during customer login for email: {}", customerLogin.getEmail());

            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
}
