package com.example.Book_Store.store.basket.repository;

import com.example.Book_Store.store.basket.entity.BasketProducts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketProductsRepository extends JpaRepository<BasketProducts, Long> {
    BasketProducts findBasketProductById(Long id);

}
