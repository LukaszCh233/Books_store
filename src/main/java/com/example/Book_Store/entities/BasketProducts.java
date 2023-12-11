package com.example.Book_Store.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer id;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "basket_id")
    private Basket basket;

    @Column
    private Integer idBook;

    @Column
    private String name;
    @Column
    private String author;

    @Column
    private Double price;

    @Column
    private Integer quantity;

    public void updateTotalPrice() {
        if (basket != null) {
            basket.setTotalPrice(basket.getTotalPrice() + (price * quantity));
        }
    }
}
