package com.example.Book_Store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "basket")
@Getter
@Setter
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idBasket;
    @Column
    private Long idBook;
    @Column
    private String name;
    @Column
    private Double price;
    @Column
    private int quantity;

    public Basket() {
    }

    public Basket(Long idBasket, Long idBook, String name, Double price, int quantity) {
        this.idBasket = idBasket;
        this.idBook = idBook;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}

