package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.Basket;
import com.example.Book_Store.repository.BasketRepository;
import com.example.Book_Store.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BasketServiceImpl implements BasketService {

    BasketRepository basketRepository;
    @Autowired
    public BasketServiceImpl(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @Override
    public Basket findByIdBook(Long id) {
        return basketRepository.findByIdBook(id);
    }
}
