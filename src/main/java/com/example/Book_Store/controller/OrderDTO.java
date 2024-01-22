package com.example.Book_Store.controller;

import com.example.Book_Store.entities.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderDTO {
    private Integer id;
    private CustomerDTO customer;
    private LocalDate orderData;
    private Double price;
    private Status status;
    private List<OrderedBooksDTO> orderedBooks;
    public void setCustomer(Customer customer) {
        this.customer = new CustomerDTO();
        this.customer.setId(customer.getId());
        this.customer.setName(customer.getName());
        this.customer.setLastName(customer.getLastName());
        this.customer.setEmail(customer.getCustomerLogin().getEmail());
        this.customer.setNumber(customer.getNumber());

    }
        public void setOrderedBooks(List<OrderedBooks> orderedBooksList) {
            this.orderedBooks = new ArrayList<>();
            for (OrderedBooks orderedBooks : orderedBooksList) {
                OrderedBooksDTO orderedBooksDTO = new OrderedBooksDTO();
                orderedBooksDTO.setIdBook(orderedBooks.getIdBook());
                orderedBooksDTO.setQuantity(orderedBooks.getQuantity());
                this.orderedBooks.add(orderedBooksDTO);
            }
    }

    @Data
    public static class CustomerDTO{
        private Integer id;
        private String name;
        private String lastName;
        private String email;
        private int number;


    }
    @Data
    public static class OrderedBooksDTO{

        private Integer idBook;
        private Integer quantity;
    }

}

