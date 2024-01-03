package com.example.Book_Store.repository;

import com.example.Book_Store.entities.OrderedBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedBooksRepository extends JpaRepository <OrderedBooks, Integer> {

}
