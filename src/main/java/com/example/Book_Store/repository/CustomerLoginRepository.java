package com.example.Book_Store.repository;

import com.example.Book_Store.entities.CustomerLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerLoginRepository extends JpaRepository<CustomerLogin, Long> {
    boolean existsByEmail(String email);

    boolean existsByPassword(String password);

    boolean existsByEmailAndPassword(String email, String password);

    CustomerLogin findByEmailAndPassword(String email, String password);

    CustomerLogin findByEmail(String email);

    CustomerLogin findPasswordByEmail(String email);

}