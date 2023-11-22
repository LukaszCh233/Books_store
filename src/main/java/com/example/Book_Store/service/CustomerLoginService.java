package com.example.Book_Store.service;

import com.example.Book_Store.entities.CustomerLogin;

public interface CustomerLoginService {
    boolean existsByEmail(String email);

    boolean existsByPassword(String password);

    boolean existsByEmailAndPassword(String email, String password);

    CustomerLogin findByEmailAndPassword(String email, String password);

    CustomerLogin findByEmail(String email);

    CustomerLogin findPasswordByEmail(String email);


}
