package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.Customer;
import com.example.Book_Store.entities.CustomerLogin;
import com.example.Book_Store.service.CustomerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtTokenServiceImpl {
    CustomerService customerService;

    @Autowired
    public JwtTokenServiceImpl(CustomerService customerService) {
        this.customerService = customerService;
    }

    SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateJwtToken(CustomerLogin customerLogin) {

        return Jwts.builder()
                .setSubject((customerLogin.getEmail()))
                .signWith(key)
                .compact();

    }

    public String extractEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean isAuthenticated(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            Date now = new Date();
            return now.before(claims.getExpiration());
        } catch (Exception e) {
            return false;
        }
    }
}