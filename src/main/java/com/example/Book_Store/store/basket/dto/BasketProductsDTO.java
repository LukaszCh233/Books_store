package com.example.Book_Store.store.basket.dto;

public record BasketProductsDTO(Long id, Long idBook, String name, String author, Double price, Long quantity) {
}
