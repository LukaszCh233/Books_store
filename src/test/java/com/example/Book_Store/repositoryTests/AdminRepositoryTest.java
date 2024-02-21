package com.example.Book_Store.repositoryTests;

import com.example.Book_Store.entities.Admin;
import com.example.Book_Store.entities.Role;
import com.example.Book_Store.repository.AdminRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
public class AdminRepositoryTest {
    @Autowired
    AdminRepository adminRepository;

    @BeforeEach
    public void setUp() {
        adminRepository.deleteAll();
    }

    @Test
    public void shouldSaveAdmin_Test() {
        //Given
        Admin admin = new Admin(3, "testName", "testEmail", "testPassword", Role.ADMIN);

        //When
        adminRepository.save(admin);

        //Then
        List<Admin> adminList = adminRepository.findAll();

        assertFalse(adminList.isEmpty());
    }

    @Test
    public void shouldFindAdminByEmail_Test() {
        //Given
        Admin admin = new Admin(1, "testName", "testEmail", "testPassword", Role.ADMIN);

        //When
        adminRepository.save(admin);

        //Then
        Optional<Admin> findAdmin = adminRepository.findByEmail(admin.getEmail());

        Assertions.assertTrue(findAdmin.isPresent());
        String foundEmail = findAdmin.get().getEmail();
        Assertions.assertEquals(foundEmail, admin.getEmail());
    }
}
