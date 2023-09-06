package com.example.Book_Store.service;

import com.example.Book_Store.entities.Basket;

public interface BasketService {
    Basket findByIdBook(Long id);
    Basket saveBasket(Basket basket);
}
