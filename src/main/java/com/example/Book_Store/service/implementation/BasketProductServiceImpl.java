package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.BasketProducts;
import com.example.Book_Store.repository.BasketProductRepository;
import com.example.Book_Store.service.BasketProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
public class BasketProductServiceImpl implements BasketProductService {
    private final BasketProductRepository basketProductRepository;
@Autowired
    public BasketProductServiceImpl(BasketProductRepository basketProductRepository) {
        this.basketProductRepository = basketProductRepository;
    }

    @Override
    public BasketProducts saveBasketProduct(BasketProducts basketProducts) {
        return basketProductRepository.save(basketProducts);
    }

    @Override
    public void deleteById(Integer id) {

        basketProductRepository.deleteById(id);
    }

    @Override
    public BasketProducts updateBasketProduct(BasketProducts basketProducts) {
        return basketProductRepository.save(basketProducts);
    }

    @Override
    public BasketProducts findBasketProductById(Integer id) {
        return basketProductRepository.findBasketProductById(id);
    }

    @Override
    public void deleteBasketProducts() {
        basketProductRepository.deleteAll();
    }


}
