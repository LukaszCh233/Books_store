package com.example.Book_Store.store.book.entity;

import com.example.Book_Store.store.Status;
import com.example.Book_Store.store.bookCategory.entity.BookCategory;
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
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "author")
    private String author;
    @Column(name = "price")
    private double price;
    @Column(name = "quantity")
    private Long quantity;
    @Column(name = "status")
    private Status status;
    @ManyToOne
    @JoinColumn(name = "bookCategory")
    private BookCategory bookCategory;

    @PrePersist
    public void setDefaultStatusIfAvailable() {
        if (quantity > 0) {
            status = Status.AVAILABLE;
        } else status = Status.LACK;
    }
}


