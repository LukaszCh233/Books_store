package com.example.Book_Store.service;

import com.example.Book_Store.entities.CustomerLogin;

public interface CustomerLoginService {
    CustomerLogin findByEmail(String email);


}
