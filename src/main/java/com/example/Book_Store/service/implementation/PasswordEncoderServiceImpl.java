package com.example.Book_Store.service.implementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordEncoderServiceImpl {
    PasswordEncoder passwordEncoder;
@Autowired
public PasswordEncoderServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String codingPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);

    }
}