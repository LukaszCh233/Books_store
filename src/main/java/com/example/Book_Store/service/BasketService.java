package com.example.Book_Store.service;

import com.example.Book_Store.controller.BasketDTO;
import com.example.Book_Store.entities.Basket;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public interface BasketService {
    void addBookToBasket(Integer idBook, Integer quantity, Principal principal);

    BasketDTO findBasketDTOByUserPrincipal(Principal principal);

    Basket findBasketByUserPrincipal(Principal principal);

    void deleteBasketById(Principal principal);

    void updateBasket(Integer productId, Integer quantity, Principal principal);
}
