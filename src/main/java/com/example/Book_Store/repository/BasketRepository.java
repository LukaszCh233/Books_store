package com.example.Book_Store.repository;

import com.example.Book_Store.entities.Basket;
import com.example.Book_Store.entities.Book;
import com.example.Book_Store.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.relational.core.sql.In;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Integer> {

    Basket findBasketByUserId(Integer id);




}
