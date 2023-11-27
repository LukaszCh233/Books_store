package com.example.Book_Store.service;

import com.example.Book_Store.entities.Admin;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface AdminService {
    Admin createAdmin(Admin admin);

    boolean existsByEmail(String email);

    Optional<Admin> findByEmail(String email);

}
