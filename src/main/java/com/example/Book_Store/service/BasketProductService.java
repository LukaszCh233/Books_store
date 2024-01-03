package com.example.Book_Store.service;

import com.example.Book_Store.entities.BasketProducts;
import org.springframework.stereotype.Service;

@Service
public interface BasketProductService {

    BasketProducts saveBasketProduct(BasketProducts basketProducts);
    void deleteById(Integer id);
    BasketProducts updateBasketProduct(BasketProducts basketProducts);
    BasketProducts findBasketProductById(Integer id);
    void deleteBasketProducts();

}
