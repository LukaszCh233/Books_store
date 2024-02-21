package com.example.Book_Store.serviceTests;

import com.example.Book_Store.entities.Admin;
import com.example.Book_Store.entities.Role;
import com.example.Book_Store.exceptions.ExistsException;
import com.example.Book_Store.repository.AdminRepository;
import com.example.Book_Store.service.implementation.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminServiceTest {

    @InjectMocks
    private AdminServiceImpl adminService;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldCreateAdmin_Successfully() {
        Admin admin = new Admin(1, "testName", "testEmail", "testPassword", null);

        when(adminRepository.findByEmail("testEmail")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("testPassword")).thenReturn("encodePassword");
        when(adminRepository.save(admin)).thenReturn(admin);

        Admin createdAdmin = adminService.createAdmin(admin);

        assertNotNull(createdAdmin);
        assertEquals(Role.ADMIN, createdAdmin.getRole());
        assertEquals("encodePassword", createdAdmin.getPassword());
        verify(adminRepository, times(1)).findByEmail("testEmail");
        verify(passwordEncoder, times(1)).encode("testPassword");
        verify(adminRepository, times(1)).save(admin);
    }

    @Test
    void shouldCreateAdmin_ExistingEmail() {
        Admin admin = new Admin(1, "testName", "testEmail", "testPassword", null);

        when(adminRepository.findByEmail("testEmail")).thenReturn(Optional.of(admin));

        assertThrows(ExistsException.class, () -> adminService.createAdmin(admin));

        verify(adminRepository, times(1)).findByEmail("testEmail");
        verify(adminRepository, times(0)).save(admin);
    }
}
