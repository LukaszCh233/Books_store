package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.CustomerLogin;
import com.example.Book_Store.repository.CustomerLoginRepository;
import com.example.Book_Store.service.CustomerLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerLoginServiceImpl implements CustomerLoginService {

    CustomerLoginRepository customerLoginRepository;
@Autowired
    public CustomerLoginServiceImpl(CustomerLoginRepository customerLoginRepository) {
        this.customerLoginRepository = customerLoginRepository;
    }

    @Override
    public boolean existsByEmail(String email) {
        return customerLoginRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPassword(String password) {
        return customerLoginRepository.existsByPassword(password);
    }

    @Override
    public boolean existsByEmailAndPassword(String email, String password) {
        return customerLoginRepository.existsByEmailAndPassword(email,password);
    }

    @Override
    public CustomerLogin findByEmailAndPassword(String email, String password) {
        return customerLoginRepository.findByEmailAndPassword(email,password);
    }

    @Override
    public CustomerLogin findByEmail(String email) {
        return customerLoginRepository.findByEmail(email);
    }

    @Override
    public CustomerLogin findPasswordByEmail(String email) {
        return customerLoginRepository.findPasswordByEmail(email);
    }
}
