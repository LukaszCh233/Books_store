package com.example.Book_Store.repositoryTest;

import com.example.Book_Store.account.Role;
import com.example.Book_Store.account.admin.Admin;
import com.example.Book_Store.account.customer.Customer;
import com.example.Book_Store.account.admin.AdminRepository;
import com.example.Book_Store.account.customer.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        adminRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void findAdminByEmail_test() {
        newAdmin("admin", "admin@example.com");

        Optional<Admin> foundAdmin = adminRepository.findByEmail("admin@example.com");

        assertTrue(foundAdmin.isPresent());
        Assertions.assertEquals(foundAdmin.get().getName(), "admin");
    }

    @Test
    public void findCustomerByEmail_test() {
        newCustomer("customer", "customer@example.com");

        Optional<Customer> foundCustomer = customerRepository.findByEmail("customer@example.com");

        assertTrue(foundCustomer.isPresent());
        Assertions.assertEquals(foundCustomer.get().getName(), "customer");
    }

    private void newAdmin(String name, String email) {
        Admin admin = new Admin();
        admin.setName(name);
        admin.setEmail(email);
        admin.setPassword("password");
        admin.setRole(Role.ADMIN);
        adminRepository.save(admin);
    }

    private void newCustomer(String name, String email) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPassword("password");
        customer.setRole(Role.ADMIN);
        customer.setLastName("customer");
        customer.setNumber(123456);
        customerRepository.save(customer);
    }
}
