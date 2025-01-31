package com.example.Book_Store.account.admin;

import com.example.Book_Store.account.Role;
import com.example.Book_Store.account.input.LoginRequest;
import com.example.Book_Store.config.HelpJwt;
import com.example.Book_Store.exceptions.ExistsException;
import com.example.Book_Store.exceptions.IncorrectPasswordException;
import com.example.Book_Store.mapper.MapperEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapperEntity mapperEntity;
    private final HelpJwt helpJwt;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder, MapperEntity mapperEntity,
                        HelpJwt helpJwt) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapperEntity = mapperEntity;
        this.helpJwt = helpJwt;
    }

    public AdminDTO createAdmin(Admin admin) {
        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            throw new ExistsException("Admin with this email is registered");
        }
        admin.setRole(Role.ADMIN);
        String encodedPassword = passwordEncoder.encode(admin.getPassword());
        admin.setPassword(encodedPassword);

        return mapperEntity.mapAdminToAdminDTO(adminRepository.save(admin));
    }

    public String adminAuthorization(LoginRequest loginRequest) {
        Admin registeredAdmin = adminRepository.findByEmail(loginRequest.getEmail()).orElseThrow(()
                -> new EntityNotFoundException("User not exists"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), registeredAdmin.getPassword())) {
            throw new IncorrectPasswordException("Incorrect email or password");
        }
        return helpJwt.generateToken(registeredAdmin);
    }
}
