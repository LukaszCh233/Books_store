package com.example.Book_Store.repository;

import com.example.Book_Store.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("SELECT c FROM Customer c JOIN c.customerLogin cl WHERE cl.email = :email")
    Optional<Customer> findByEmail(@Param("email") String email);

}


