package com.example.Book_Store.serviceTests;

import com.example.Book_Store.controller.CustomerDTO;
import com.example.Book_Store.entities.Customer;
import com.example.Book_Store.entities.CustomerLogin;
import com.example.Book_Store.entities.Role;
import com.example.Book_Store.exceptions.ExistsException;
import com.example.Book_Store.repository.CustomerRepository;
import com.example.Book_Store.service.implementation.CustomerServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerServiceTest {
    @InjectMocks
    private CustomerServiceImpl customerService;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldFindAllCustomers_Existing_Test() {
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        List<Customer> mockCustomer = List.of(
                new Customer(1, "testName", "testLastName", customerLogin, 123456, Role.CUSTOMER));
        when(customerRepository.findAll()).thenReturn(mockCustomer);

        List<CustomerDTO> resultCustomers = customerService.findAllCustomers();

        List<CustomerDTO> expectedCustomerDTOs = mockCustomer.stream()
                .map(customerService::mapCustomerToCustomerDTO)
                .toList();

        verify(customerRepository, times(1)).findAll();

        assertIterableEquals(expectedCustomerDTOs, resultCustomers);
    }

    @Test
    void shouldFindAllCustomers_NotExisting_Test() {

        when(customerRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> customerService.findAllCustomers());

        verify(customerRepository, times(1)).findAll();
    }

    @Test
    void shouldCreateCustomer_Successfully() {
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer newCustomer = new Customer(1, "TestName", "TestLastName", customerLogin, 123456, null);

        when(customerRepository.findByEmail("testEmail")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");
        when(customerRepository.save(newCustomer)).thenReturn(newCustomer);

        Customer createdCustomer = customerService.createCustomer(newCustomer);

        assertNotNull(createdCustomer);
        assertEquals(Role.CUSTOMER, createdCustomer.getRole());
        assertEquals("encodedPassword", createdCustomer.getCustomerLogin().getPassword());
        verify(customerRepository, times(1)).findByEmail("testEmail");
        verify(passwordEncoder, times(1)).encode("testPassword");
        verify(customerRepository, times(1)).save(newCustomer);
    }

    @Test
    void shouldCreateCustomer_ExistingEmail() {
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer newCustomer = new Customer(1, "TestName", "TestLastName", customerLogin, 123456, null);

        when(customerRepository.findByEmail("testEmail")).thenReturn(Optional.of(newCustomer));

        assertThrows(ExistsException.class, () -> customerService.createCustomer(newCustomer));
        verify(customerRepository, times(1)).findByEmail("testEmail");
        verify(customerRepository, times(0)).save(newCustomer);
    }
}