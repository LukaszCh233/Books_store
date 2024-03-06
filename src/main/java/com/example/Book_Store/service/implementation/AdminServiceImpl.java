package com.example.Book_Store.service.implementation;

import com.example.Book_Store.controller.AdminDTO;
import com.example.Book_Store.entities.Admin;
import com.example.Book_Store.entities.Role;
import com.example.Book_Store.exceptions.ExistsException;
import com.example.Book_Store.repository.AdminRepository;
import com.example.Book_Store.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Admin createAdmin(Admin admin) {

        findByEmail(admin.getEmail())
                .ifPresent(existingAdmin -> {
                    throw new ExistsException("Admin exists");
                });
        admin.setRole(Role.ADMIN);
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);
        return adminRepository.save(admin);
    }

    public AdminDTO mapAdminToAdminDTO(Admin admin) {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setId(admin.getId());
        adminDTO.setName(admin.getName());
        adminDTO.setEmail(admin.getEmail());
        return adminDTO;
    }

    @Override
    public Optional<Admin> findByEmail(String email) {
        return adminRepository.findByEmail(email);
    }

}
