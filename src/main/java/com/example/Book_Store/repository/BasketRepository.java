package com.example.Book_Store.repository;

import com.example.Book_Store.entities.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    Basket findByIdBook(Long id);
}
