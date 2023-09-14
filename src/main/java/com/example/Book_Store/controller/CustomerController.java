package com.example.Book_Store.controller;

import ch.qos.logback.classic.Logger;
import com.example.Book_Store.entities.*;
import com.example.Book_Store.service.*;
import com.example.Book_Store.service.implementation.JwtTokenServiceImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.security.PermitAll;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final BookService bookService;
    private final CategoryService categoryService;
    private final CustomerService customerService;
    private final OrderService orderService;
    private final CustomerLoginService customerLoginService;
    private final BasketService basketService;
    private final JwtTokenServiceImpl jwtTokenService;
    private final Logger logger = (Logger) LoggerFactory.getLogger(CustomerController.class);


    @Autowired
    public CustomerController(BookService bookService, CategoryService categoryService,
                              CustomerService customerService, OrderService orderService,
                              CustomerLoginService customerLoginService,BasketService basketService, JwtTokenServiceImpl jwtTokenService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.customerLoginService = customerLoginService;
        this.basketService = basketService;
        this.jwtTokenService = jwtTokenService;
    }

    @GetMapping("/loggedCustomer")
    public ResponseEntity<Customer> getDataLoggedCustomer(@RequestHeader ("Authorization") String token ) {

        String email = jwtTokenService.extractEmailFromToken(token);

        if (email != null) {
            Customer loggedCustomer = customerService.findByEmail(email);
            if (loggedCustomer != null) {
                logger.info("Klient o adresie e-mail {} został pomyślnie uwierzytelniony.", email);

                return new ResponseEntity<>(loggedCustomer, HttpStatus.OK);
            }
        }
        logger.error("Błąd podczas uwierzytelniania klienta z adresem e-mail: {}", token);

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
    @PostMapping("/toBasket/{id}/{quantity}")
    public ResponseEntity<String> addBookById(@RequestHeader ("Authorization") String token, @PathVariable Long id, @PathVariable int quantity) {
        logger.debug("Received a request to add a book with ID {} to the basket.", id);

        if (jwtTokenService.isAuthenticated(token)) {


            Book checkBook = bookService.findBookById(id);

            if (checkBook != null && checkBook.getQuantity() >= quantity) {
                logger.debug("Found the book with ID {}. Available quantity: {}", id, checkBook.getQuantity());

                checkBook.setQuantity(checkBook.getQuantity() - quantity);
                Basket basket = new Basket(null, checkBook.getId(), checkBook.getTitle(), checkBook.getPrice(), quantity);
                basketService.saveBasket(basket);
                if (checkBook.getQuantity() > 0) {
                    checkBook.setStatus(Status.AVAILABLE);
                } else {
                    checkBook.setStatus(Status.LACK);
                }
                bookService.updateBook(checkBook);
                logger.info("Book with ID {} added to the basket successfully.", id);

                return new ResponseEntity<>("Book has benn added to basket", HttpStatus.OK);
            } else {
                logger.warn("The book with ID {} does not exist or is not available in sufficient quantity.", id);

                return new ResponseEntity<>("The book does not exist or is not available in sufficient quantity.", HttpStatus.NOT_FOUND);
            }
        }
        logger.warn("Unauthorized access with token: {}", token);

        return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);

    }
}
