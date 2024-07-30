package com.example.Book_Store.basket.dto;

import java.util.List;


public record BasketDTO(Long idBasket, Long idUser, Double totalPrice, List<BasketProductsDTO> basketProductsList) {

}