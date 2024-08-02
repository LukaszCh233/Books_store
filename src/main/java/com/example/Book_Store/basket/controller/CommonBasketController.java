package com.example.Book_Store.basket.controller;

import com.example.Book_Store.basket.dto.BasketDTO;
import com.example.Book_Store.basket.service.BasketService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/common")
public class CommonBasketController {
    private final BasketService basketService;

    public CommonBasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping("/basket/{idBook}/{quantity}")
    public ResponseEntity<String> addBookToBasket(@PathVariable Long idBook, @PathVariable Long quantity, Principal principal) {
        basketService.addBookToBasket(idBook, quantity, principal);

        return ResponseEntity.ok("Book added to basket");
    }

    @DeleteMapping("/basket")
    public ResponseEntity<String> deleteBasket(Principal principal) {
        basketService.deleteBasketByPrincipal(principal);

        return ResponseEntity.ok("Basket deleted successfully");
    }

    @GetMapping("/basket")
    public ResponseEntity<BasketDTO> displayBasket(Principal principal) {
        BasketDTO basketDTO = basketService.findBasketDTOByUserPrincipal(principal);

        return ResponseEntity.ok(basketDTO);
    }

    @PutMapping("/basket/{id}/{quantity}")
    public ResponseEntity<String> updateQuantityBookInBasket(@PathVariable Long id, @PathVariable Long quantity, Principal principal) {
        basketService.updateBasketProductQuantity(id, quantity, principal);

        return ResponseEntity.ok("Quantity update");
    }

    @DeleteMapping("/logout")
    public ResponseEntity<String> logoutDeleteBasket(Principal principal) {
        basketService.deleteBasketByPrincipal(principal);

        return ResponseEntity.ok("Logout successful");
    }
}
