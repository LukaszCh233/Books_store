package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.CustomerLogin;
import com.example.Book_Store.repository.CustomerLoginRepository;
import com.example.Book_Store.service.CustomerLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerLoginServiceImpl implements CustomerLoginService {

   private final CustomerLoginRepository customerLoginRepository;

    @Autowired
    public CustomerLoginServiceImpl(CustomerLoginRepository customerLoginRepository) {
        this.customerLoginRepository = customerLoginRepository;
    }

    @Override
    public CustomerLogin findByEmail(String email) {
        return customerLoginRepository.findByEmail(email);
    }

}
