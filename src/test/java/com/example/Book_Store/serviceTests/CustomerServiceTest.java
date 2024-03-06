package com.example.Book_Store.serviceTests;

import com.example.Book_Store.controller.CustomerDTO;
import com.example.Book_Store.entities.Customer;
import com.example.Book_Store.entities.CustomerLogin;
import com.example.Book_Store.entities.Role;
import com.example.Book_Store.exceptions.ExistsException;
import com.example.Book_Store.repository.CustomerRepository;
import com.example.Book_Store.service.implementation.CustomerServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerServiceTest {

    private final CustomerServiceImpl customerService;
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceTest(CustomerServiceImpl customerService, CustomerRepository customerRepository) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
    }

    @BeforeEach
    public void setUp() {

        customerRepository.deleteAll();
    }

    @Test
    void shouldFindAllCustomers_Existing_Test() {
        //Given
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        List<Customer> customers = List.of(
                new Customer(null, "testName", "testLastName", customerLogin, 123456, Role.CUSTOMER));

        customerRepository.saveAll(customers);

        List<CustomerDTO> expectedCustomerDTOs = customers.stream()
                .map(customerService::mapCustomerToCustomerDTO)
                .toList();
        //When
        List<CustomerDTO> resultCustomers = customerService.findAllCustomers();

        //Then
        assertEquals(1, resultCustomers.size());
        assertEquals(expectedCustomerDTOs.get(0).getId(), resultCustomers.get(0).getId());
        assertEquals(expectedCustomerDTOs.get(0).getName(), resultCustomers.get(0).getName());
        assertEquals(expectedCustomerDTOs.get(0).getEmail(), resultCustomers.get(0).getEmail());
        assertEquals(expectedCustomerDTOs.get(0).getNumber(), resultCustomers.get(0).getNumber());
        assertEquals(expectedCustomerDTOs.get(0).getLastName(), resultCustomers.get(0).getLastName());
    }

    @Test
    void shouldFindAllCustomers_NotExisting_Test() {

        assertThrows(EntityNotFoundException.class, () -> customerService.findAllCustomers());
    }

    @Test
    void shouldCreateCustomer_Successfully() {
        //Given
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(null, "TestName", "TestLastName", customerLogin, 123456, null);

        //When
        Customer createdCustomer = customerService.createCustomer(customer);

        //Then
        assertNotNull(createdCustomer);
        assertEquals(Role.CUSTOMER, createdCustomer.getRole());
        assertEquals(customer.getName(), createdCustomer.getName());
        assertEquals(customer.getLastName(), createdCustomer.getLastName());
        assertEquals(customer.getId(), createdCustomer.getId());
        assertEquals(customer.getCustomerLogin().getEmail(), createdCustomer.getCustomerLogin().getEmail());
    }

    @Test
    void shouldCreateCustomer_ExistingEmail() {
        //Given
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(null, "TestName", "TestLastName", customerLogin, 123456, null);

        customerRepository.save(customer);
        //when, Then
        assertThrows(ExistsException.class, () -> customerService.createCustomer(customer));
    }
}