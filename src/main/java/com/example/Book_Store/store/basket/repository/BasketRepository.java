package com.example.Book_Store.store.basket.repository;

import com.example.Book_Store.store.basket.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Optional<Basket> findBasketByUserId(Long id);
}
