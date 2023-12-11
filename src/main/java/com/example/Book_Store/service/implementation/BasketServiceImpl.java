package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.Basket;
import com.example.Book_Store.entities.Book;
import com.example.Book_Store.repository.BasketRepository;
import com.example.Book_Store.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BasketServiceImpl implements BasketService {

   private final BasketRepository basketRepository;

    @Autowired
    public BasketServiceImpl(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @Override
    public Basket findByIdBook(Integer id) {
        return null;
    }

    @Override
    public Basket saveBasket(Basket basket) {
        return basketRepository.save(basket);
    }

    @Override
    public Basket findBasketByUserId(Integer id) {
        return basketRepository.findBasketByUserId(id);
    }


}





