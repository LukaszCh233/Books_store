package com.example.Book_Store.repositoryTests;

import com.example.Book_Store.entities.Customer;
import com.example.Book_Store.entities.CustomerLogin;
import com.example.Book_Store.entities.Role;
import com.example.Book_Store.repository.CustomerLoginRepository;
import com.example.Book_Store.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    CustomerLoginRepository customerLoginRepository;

    @BeforeEach
    public void setUp() {
        customerRepository.deleteAll();
        customerLoginRepository.deleteAll();


    }

    @Test
    void shouldSaveCustomer_Test() {
        //Given
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(null, "testName", "testLastName", customerLogin, 1234, Role.CUSTOMER);

        //When
        customerRepository.save(customer);

        //Then
        List<Customer> customers = customerRepository.findAll();

        assertFalse(customers.isEmpty());
        assertEquals(1, customers.size());
    }

    @Test
    void shouldFindCustomerByEmail_Test() {
        //Given
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(null, "testName", "testLastName", customerLogin, 1234, Role.CUSTOMER);

        //When
        customerRepository.save(customer);

        //Then
        Optional<Customer> foundCustomer = customerRepository.findByEmail(customer.getCustomerLogin().getEmail());

        assertTrue(foundCustomer.isPresent());
        assertEquals(customer.getId(), foundCustomer.get().getId());
    }

    @Test
    void shouldFindAllCustomers_Test() {
        //Given
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(null, "testName", "testLastName", customerLogin, 1234, Role.CUSTOMER);

        //When
        customerRepository.save(customer);

        //Then
        List<Customer> customers = customerRepository.findAll();

        assertNotNull(customers);
        assertEquals(1, customers.size());
    }

    @Test
    void shouldFindCustomerById_Test() {
        //Given
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(null, "testName", "testLastName", customerLogin, 1234, Role.CUSTOMER);

        //When
        customerRepository.save(customer);

        //Then
        Optional<Customer> foundCustomer = customerRepository.findById(customer.getId());

        assertTrue(foundCustomer.isPresent());
        assertEquals(customer.getId(), foundCustomer.get().getId());
        assertEquals(customer.getName(), foundCustomer.get().getName());
        assertEquals(customer.getLastName(), foundCustomer.get().getLastName());
    }
}
