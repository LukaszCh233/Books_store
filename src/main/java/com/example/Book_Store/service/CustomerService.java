package com.example.Book_Store.service;

import com.example.Book_Store.entities.Customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CustomerService {
    @Query("SELECT c FROM Customer c JOIN c.customerLogin cl WHERE cl.email = :email")
    Customer findByEmail(@Param("email") String email);
    List<Customer> findAllCustomers();
    Customer createCustomer(Customer customer);
}
