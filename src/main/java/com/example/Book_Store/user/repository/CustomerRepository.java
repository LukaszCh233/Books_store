package com.example.Book_Store.user.repository;

import com.example.Book_Store.user.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c JOIN c.customerLogin cl WHERE cl.email = :email")
    Optional<Customer> findByEmail(@Param("email") String email);
}


