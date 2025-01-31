package com.example.Book_Store.serviceTest;

import com.example.Book_Store.account.Role;
import com.example.Book_Store.store.order.repository.OrderRepository;
import com.example.Book_Store.account.customer.CustomerDTO;
import com.example.Book_Store.account.admin.Admin;
import com.example.Book_Store.account.customer.Customer;
import com.example.Book_Store.account.input.LoginRequest;
import com.example.Book_Store.account.admin.AdminRepository;
import com.example.Book_Store.account.customer.CustomerRepository;
import com.example.Book_Store.account.admin.AdminService;
import com.example.Book_Store.account.customer.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    @Autowired
    AdminService adminService;
    @Autowired
    CustomerService customerService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();
        adminRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void whenCustomerAuthorizationIsCorrectShouldGenerateToken_test() {
        newCustomer("customer@example.com", "customerPassword");
        LoginRequest loginRequest = new LoginRequest("customer@example.com", "customerPassword");

        String token = customerService.customerAuthorization(loginRequest);

        Assertions.assertNotNull(token);
    }

    @Test
    public void whenAdminAuthorizationIsCorrectShouldGenerateToken_test() {
        newAdmin("admin@example.com", "adminPassword");
        LoginRequest loginRequest = new LoginRequest("admin@example.com", "adminPassword");

        String token = adminService.adminAuthorization(loginRequest);

        Assertions.assertNotNull(token);
    }

    @Test
    public void createdCustomerShouldBeFindICustomersList_test() {
        newCustomer("customer@example.com", "password");

        List<CustomerDTO> customerList = customerService.findAllCustomers();

        Assertions.assertEquals(customerList.size(), 1);
        Assertions.assertEquals(customerList.get(0).email(), "customer@example.com");
    }

    private void newCustomer(String email, String password) {
        Customer customer = new Customer();
        customer.setName("name");
        customer.setLastName("lastName");
        customer.setEmail(email);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setRole(Role.CUSTOMER);

        customerRepository.save(customer);
    }

    private void newAdmin(String email, String password) {
        Admin admin = new Admin();
        admin.setName("name");
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole(Role.ADMIN);

        adminRepository.save(admin);
    }
}

