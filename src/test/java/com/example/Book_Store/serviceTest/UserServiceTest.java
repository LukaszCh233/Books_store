package com.example.Book_Store.serviceTest;

import com.example.Book_Store.enums.Role;
import com.example.Book_Store.user.dto.CustomerDTO;
import com.example.Book_Store.user.entity.Admin;
import com.example.Book_Store.user.entity.Customer;
import com.example.Book_Store.user.entity.CustomerLogin;
import com.example.Book_Store.user.entity.LoginRequest;
import com.example.Book_Store.user.repository.AdminRepository;
import com.example.Book_Store.user.repository.CustomerRepository;
import com.example.Book_Store.user.service.AdminService;
import com.example.Book_Store.user.service.CustomerService;
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

    @BeforeEach
    public void setUp() {
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

    private void newCustomer(String email, String password) {
        Customer customer = new Customer();
        customer.setName("name");
        customer.setLastName("lastName");
        customer.setCustomerLogin(new CustomerLogin(email, passwordEncoder.encode(password)));
        customer.setRole(Role.CUSTOMER);

        customerRepository.save(customer);
    }
    @Test
    public void createdUserShouldBeFindInUsersList_test() {
        newCustomer("customer@example.com", "password");

        List<CustomerDTO> customerList = customerService.findAllCustomers();

        Assertions.assertEquals(customerList.size(), 1);
        Assertions.assertEquals(customerList.get(0).email(), "customer@example.com");
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

