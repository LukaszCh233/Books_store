package com.example.Book_Store.account.customer;

import com.example.Book_Store.account.Role;
import com.example.Book_Store.account.input.LoginRequest;
import com.example.Book_Store.config.HelpJwt;
import com.example.Book_Store.exceptions.ExistsException;
import com.example.Book_Store.exceptions.IncorrectPasswordException;
import com.example.Book_Store.mapper.MapperEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapperEntity mapperEntity;
    private final HelpJwt helpJwt;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder,
                           MapperEntity mapperEntity, HelpJwt helpJwt) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapperEntity = mapperEntity;
        this.helpJwt = helpJwt;
    }

    public List<CustomerDTO> findAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) {
            throw new EntityNotFoundException("Customer list is empty");
        }
        return mapperEntity.mapCustomersToCustomersDTO(customers);
    }

    public CustomerDTO createCustomer(Customer customer) {
        if (customerRepository.findByEmail(customer.getEmail()).isPresent()) {
            throw new ExistsException("Customer with this email is registered");
        }
        customer.setRole(Role.CUSTOMER);
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodedPassword);

        return mapperEntity.mapCustomerToCustomerDTO(customerRepository.save(customer));
    }

    public String customerAuthorization(LoginRequest loginRequest) {
        Customer registeredUser = customerRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()
                -> new EntityNotFoundException("User not exists"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), registeredUser.getPassword())) {
            throw new IncorrectPasswordException("Incorrect email or password");
        }
        return helpJwt.generateToken(registeredUser);
    }
}
