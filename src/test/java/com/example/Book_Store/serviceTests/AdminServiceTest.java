package com.example.Book_Store.serviceTests;

import com.example.Book_Store.entities.Admin;
import com.example.Book_Store.entities.Role;
import com.example.Book_Store.exceptions.ExistsException;
import com.example.Book_Store.repository.AdminRepository;
import com.example.Book_Store.service.implementation.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminServiceTest {

    private final AdminServiceImpl adminService;
    private final AdminRepository adminRepository;

    @Autowired
    public AdminServiceTest(AdminServiceImpl adminService, AdminRepository adminRepository) {
        this.adminService = adminService;
        this.adminRepository = adminRepository;
    }

    @BeforeEach
    public void setUp() {
        adminRepository.deleteAll();
    }

    @Test
    void shouldCreateAdmin_Successfully() {
        //Given
        Admin admin = new Admin(null, "testName", "testEmail", "testPassword", null);

        //When
        Admin createdAdmin = adminService.createAdmin(admin);

        //Then
        assertNotNull(createdAdmin);
        assertEquals(Role.ADMIN, createdAdmin.getRole());
        assertEquals(createdAdmin.getEmail(), admin.getEmail());
        assertEquals(createdAdmin.getId(), admin.getId());
        assertEquals(createdAdmin.getName(), admin.getName());
    }

    @Test
    void shouldCreateAdmin_ExistingEmail() {
        //Given
        Admin admin = new Admin(1, "testName", "testEmail", "testPassword", null);

        //When
        adminService.createAdmin(admin);

        //Then
        assertThrows(ExistsException.class, () -> adminService.createAdmin(admin));
    }
}
