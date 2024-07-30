package com.example.Book_Store.basket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "basket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Basket {
    @Column
    Double totalPrice;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idBasket;
    @Column
    private Long userId;
    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BasketProducts> basketProducts;

    public void updateTotalPrice(Basket basket) {
        if (basket != null && basket.getBasketProducts() != null) {
            double totalPrice = basket.getBasketProducts().stream()
                    .mapToDouble(product -> product.getPrice() * product.getQuantity())
                    .sum();
            basket.setTotalPrice(totalPrice);
        }
    }
}





