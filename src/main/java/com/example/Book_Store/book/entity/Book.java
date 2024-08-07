package com.example.Book_Store.book.entity;

import com.example.Book_Store.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank(message = "Title cannot be blank")
    @Column(name = "title")
    private String title;
    @NotBlank(message = "Author cannot be blank")
    @Column(name = "author")
    private String author;
    @NotNull(message = "Price cannot be null")
    @Column(name = "price")
    private double price;
    @Column(name = "quantity")
    private Long quantity;
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


