package com.example.Book_Store.controller;

import com.example.Book_Store.entities.CustomerLogin;
import com.example.Book_Store.entities.Role;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class CustomerDTO {

    private Integer id;

    private String name;

    private String lastName;

    private String email;

    private int number;

}
