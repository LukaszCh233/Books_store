package com.example.Book_Store.controller;

import com.example.Book_Store.entities.Basket;
import com.example.Book_Store.entities.BasketProducts;
import com.example.Book_Store.entities.OrderedBooks;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class BasketDTO {

    private Integer idBasket;
    private Integer userId;
    double totalPrice;
    private List<BasketProductsDTO> basketProducts;
    @Data
    public static class BasketProductsDTO{
        private Integer id;

        private Integer idBook;

        private String name;

        private String author;

        private Double price;

        private Integer quantity;

    }
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
}

