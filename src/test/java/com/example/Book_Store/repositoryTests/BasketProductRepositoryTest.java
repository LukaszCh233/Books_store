package com.example.Book_Store.repositoryTests;

import com.example.Book_Store.entities.BasketProducts;
import com.example.Book_Store.repository.BasketProductRepository;
import com.example.Book_Store.repository.BasketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@ActiveProfiles("test")

public class BasketProductRepositoryTest {
    @Autowired
    BasketProductRepository basketProductRepository;
    @Autowired
    BasketRepository basketRepository;

    @BeforeEach
    public void setUp() {

        basketProductRepository.deleteAll();
    }

    @Test
    void shouldFindBasketProductById_Test() {
        //Given
        BasketProducts basketProducts = new BasketProducts();

        //When
        basketProductRepository.save(basketProducts);

        //Then
        BasketProducts basketProductsList = basketProductRepository.findBasketProductById(basketProducts.getId());

        assertNotNull(basketProductsList);
    }

    @Test
    void shouldDeleteById_Test() {
        //Given
        BasketProducts basketProducts = new BasketProducts();

        //When
        basketProductRepository.save(basketProducts);

        //Then
        basketProductRepository.deleteById(basketProducts.getId());
        BasketProducts basketProductsList = basketProductRepository.findBasketProductById(basketProducts.getId());

        assertNull(basketProductsList);
    }

    @Test
    void shouldSaveBasketProduct_Test() {
        //Given
        BasketProducts basketProducts = new BasketProducts();

        //When
        basketProductRepository.save(basketProducts);

        //Then
        BasketProducts basketProductsList = basketProductRepository.findBasketProductById(basketProducts.getId());

        assertNotNull(basketProductsList);
    }
}
