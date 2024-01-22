package com.example.Book_Store.service;

import com.example.Book_Store.controller.BasketDTO;
import com.example.Book_Store.entities.Basket;
import com.example.Book_Store.entities.Book;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
public interface BasketService {
    Basket findByIdBook(Integer id);

    Basket saveBasket(Basket basket);

   void addBookToBasket(Integer idBook, Integer quantity, Principal principal);

    BasketDTO findBasketDTOByUserId(Principal principal);
    Basket findBasketByUserId(Principal principal);
    void deleteBasketById(Principal principal);
    void updateBasket(Integer productId, Integer quantity, Principal principal);
}
