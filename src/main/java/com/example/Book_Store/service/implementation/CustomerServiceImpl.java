package com.example.Book_Store.service.implementation;

import com.example.Book_Store.controller.CustomerDTO;
import com.example.Book_Store.entities.Customer;
import com.example.Book_Store.entities.Role;
import com.example.Book_Store.repository.CustomerRepository;
import com.example.Book_Store.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public List<CustomerDTO> findAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            throw new ResourceNotFoundException("Customer list is empty");
        }
        return
         customers.stream()
                .map(this::mapCustomerToCustomerDTO)
                .toList();

    }

    @Override
    public Customer createCustomer(Customer customer) {
        findByEmail(customer.getCustomerLogin().getEmail())
                .ifPresent(existingCustomer -> {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Customer exists");
                });
        customer.setRole(Role.CUSTOMER);
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.getCustomerLogin().setPassword(encodedPassword);

        return customerRepository.save(customer);
    }


    public CustomerDTO mapCustomerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setId(customer.getId());
        customerDTO.setName(customer.getName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getCustomerLogin().getEmail());
        customerDTO.setNumber(customer.getNumber());
        return customerDTO;
    }
}
