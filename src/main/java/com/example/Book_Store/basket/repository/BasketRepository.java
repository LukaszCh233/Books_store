package com.example.Book_Store.basket.repository;

import com.example.Book_Store.basket.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Basket findBasketByUserId(Long id);
}
