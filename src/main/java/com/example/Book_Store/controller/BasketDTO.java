package com.example.Book_Store.controller;

import com.example.Book_Store.entities.BasketProducts;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BasketDTO {

    private double totalPrice;
    private Integer idBasket;
    private Integer userId;
    private List<BasketProductsDTO> basketProducts;

    public void setBasketProducts(List<BasketProducts> basketProductsList) {
        this.basketProducts = new ArrayList<>();
        for (BasketProducts basketProducts : basketProductsList) {
            BasketProductsDTO basketProductsDTO = new BasketProductsDTO();
            basketProductsDTO.setId(basketProducts.getId());
            basketProductsDTO.setIdBook(basketProducts.getIdBook());
            basketProductsDTO.setQuantity(basketProducts.getQuantity());
            basketProductsDTO.setPrice(basketProducts.getPrice());
            basketProductsDTO.setName(basketProducts.getName());
            basketProductsDTO.setAuthor(basketProducts.getAuthor());
            this.basketProducts.add(basketProductsDTO);
        }
    }

    @Data
    public static class BasketProductsDTO {
        private Integer id;

        private Integer idBook;

        private String name;

        private String author;

        private Double price;

        private Integer quantity;
    }
}

