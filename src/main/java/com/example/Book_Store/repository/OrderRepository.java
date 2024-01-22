package com.example.Book_Store.repository;

import com.example.Book_Store.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    boolean existsById(Integer id);

}
