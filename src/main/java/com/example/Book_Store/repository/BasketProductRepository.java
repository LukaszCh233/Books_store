package com.example.Book_Store.repository;

import com.example.Book_Store.entities.Basket;
import com.example.Book_Store.entities.BasketProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketProductRepository extends JpaRepository<BasketProducts, Integer> {
    void deleteById(Integer id);
    BasketProducts findBasketProductById(Integer id);

}
