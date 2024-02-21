package com.example.Book_Store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
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

    @PrePersist
    public void setDefaultStatusIfAvailable() {
        if (quantity > 0) {
            status = Status.AVAILABLE;
        } else status = Status.LACK;
    }
}


