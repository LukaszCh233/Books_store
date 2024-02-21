package com.example.Book_Store.repositoryTests;

import com.example.Book_Store.entities.Basket;
import com.example.Book_Store.entities.BasketProducts;
import com.example.Book_Store.repository.BasketProductRepository;
import com.example.Book_Store.repository.BasketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class BasketRepositoryTest {
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    BasketProductRepository basketProductRepository;

    @BeforeEach
    public void setUp() {
        basketRepository.deleteAll();
    }

    @Test
    void shouldSaveBasket_Test() {
        //Given
        BasketProducts basketProducts = new BasketProducts();
        Basket basket = new Basket(null, null, 1, Collections.singletonList(basketProducts));

        //When
        basketRepository.save(basket);

        //Then
        List<Basket> baskets = basketRepository.findAll();

        assertFalse(baskets.isEmpty());
        assertEquals(1, baskets.size());
    }

    @Test
    void shouldFindBasketByUserId_Test() {
        //Given
        BasketProducts basketProducts = new BasketProducts();
        Basket basket = new Basket(null, null, 1, Collections.singletonList(basketProducts));

        //When
        basketRepository.save(basket);

        //Then
        Basket foundBasket = basketRepository.findBasketByUserId(basket.getUserId());

        assertNotNull(foundBasket);
        assertEquals(foundBasket.getUserId(), basket.getUserId());
        assertEquals(foundBasket.getIdBasket(), basket.getIdBasket());
    }

    @Test
    void shouldDeleteBasketById_Test() {
        //Given
        BasketProducts basketProducts = new BasketProducts();
        Basket basket = new Basket(null, null, 1, Collections.singletonList(basketProducts));

        //When
        basketRepository.save(basket);

        //Then
        basketRepository.deleteById(basket.getIdBasket());

        List<Basket> baskets = basketRepository.findAll();

        assertTrue(baskets.isEmpty());
    }
}
