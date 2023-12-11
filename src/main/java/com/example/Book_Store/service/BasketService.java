package com.example.Book_Store.service;

import com.example.Book_Store.entities.Basket;
import com.example.Book_Store.entities.Book;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface BasketService {
    Basket findByIdBook(Integer id);

    Basket saveBasket(Basket basket);




    Basket findBasketByUserId(Integer id);
}
