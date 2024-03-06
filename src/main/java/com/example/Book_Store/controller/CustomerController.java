package com.example.Book_Store.controller;

import com.example.Book_Store.entities.Order;
import com.example.Book_Store.service.implementation.BasketServiceImpl;
import com.example.Book_Store.service.implementation.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/customer")
public class CustomerController {

    private final OrderServiceImpl orderService;
    private final BasketServiceImpl basketService;

    @Autowired
    public CustomerController(OrderServiceImpl orderService, BasketServiceImpl basketService) {

        this.orderService = orderService;
        this.basketService = basketService;

    }

    @PostMapping("/toBasket/{idBook}/{quantity}")
    public ResponseEntity<String> addBookToBasket(@PathVariable Integer idBook, @PathVariable Integer quantity, Principal principal) {

        basketService.addBookToBasket(idBook, quantity, principal);
        return ResponseEntity.ok("Book added to basket");
    }

    @PostMapping("/order")
    public ResponseEntity<?> orderBooks(Order order, Principal principal) {

        orderService.saveOrder(order, principal);
        return ResponseEntity.ok("Order is completed");
    }

    @DeleteMapping("/deleteBasket")
    public ResponseEntity<?> deleteBasket(Principal principal) {

        basketService.deleteBasketByPrincipal(principal);
        return ResponseEntity.ok("Basket deleted successfully");
    }

    @GetMapping("/getBasket")
    public ResponseEntity<BasketDTO> displayBasket(Principal principal) {

        BasketDTO basketDTO = basketService.findBasketDTOByUserPrincipal(principal);
        return ResponseEntity.ok(basketDTO);
    }

    @PutMapping("/updateBasket/{id}/{quantity}")
    public ResponseEntity<?> updateBasket(@PathVariable Integer id, @PathVariable Integer quantity, Principal principal) {

        basketService.updateBasket(id, quantity, principal);
        return ResponseEntity.ok("Quantity update");
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logoutDeleteBasket(Principal principal) {

        basketService.deleteBasketByPrincipal(principal);
        return ResponseEntity.ok("Logout successful");
    }
}
