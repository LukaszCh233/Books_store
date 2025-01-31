package com.example.Book_Store.store.basket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "basket_products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasketProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    @ManyToOne
    @JoinColumn(name = "basket_id")
    private Basket basket;
    @Column
    private Long idBook;
    @Column
    private String name;
    @Column
    private String author;
    @Column
    private Double price;
    @Column
    private Long quantity;
}
