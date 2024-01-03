package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.Customer;
import com.example.Book_Store.entities.Role;
import com.example.Book_Store.repository.CustomerRepository;
import com.example.Book_Store.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public List<Customer> findAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer createCustomer(Customer customer) {
        customer.setRole(Role.CUSTOMER);
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.getCustomerLogin().setPassword(encodedPassword);
        return customerRepository.save(customer);
    }

    @Override
    public Customer findByName(String name) {
        return customerRepository.findByName(name);
    }
}
