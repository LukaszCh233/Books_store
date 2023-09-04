package com.example.Book_Store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

    @Entity
    @Table(name = "books")
    @Getter
    @Setter
    public class Book {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;
        @Column(name = "title")
        private String title;
        @Column(name = "author")
        private String author;
        @Column(name = "price")
        private double price;
        @Column(name = "quantity")
        private int quantity;
        @Column(name = "status")
        private Status status;
        @ManyToOne
        @JoinColumn(name = "category")
        private Category category;


        public Book() {
        }

        public Book(Long id, String title, String author, double price, int quantity, Status status, Category category) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.price = price;
            this.quantity = quantity;
            this.status = status;
            this.category = category;
        }

        @PrePersist
        public void setDefaultStatusIfAvailable() {
            if (quantity > 0) {
                status = Status.AVAILABLE;
            } else status = Status.LACK;
        }
    }


