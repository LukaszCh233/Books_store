package com.example.Book_Store.repositoryTest;

import com.example.Book_Store.account.Role;
import com.example.Book_Store.account.customer.Customer;
import com.example.Book_Store.account.customer.CustomerRepository;
import com.example.Book_Store.store.Status;
import com.example.Book_Store.store.basket.entity.Basket;
import com.example.Book_Store.store.basket.entity.BasketProducts;
import com.example.Book_Store.store.basket.repository.BasketProductsRepository;
import com.example.Book_Store.store.basket.repository.BasketRepository;
import com.example.Book_Store.store.book.entity.Book;
import com.example.Book_Store.store.book.repository.BookRepository;
import com.example.Book_Store.store.bookCategory.entity.BookCategory;
import com.example.Book_Store.store.bookCategory.repository.BookCategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class BasketRepositoryTest {
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookCategoryRepository bookCategoryRepository;
    @Autowired
    BasketProductsRepository basketProductsRepository;

    @BeforeEach
    public void setUp() {
        customerRepository.deleteAll();
        basketRepository.deleteAll();
    }

    @Test
    public void findBasketByUserId_test() {
        Basket expectedBasket = newBasket();

        Optional<Basket> foundBasket = basketRepository.findBasketByUserId(expectedBasket.getUserId());

        Assertions.assertTrue(foundBasket.isPresent());
        Assertions.assertEquals(expectedBasket.getUserId(), foundBasket.get().getUserId(), "User IDs should match");
    }

    @Test
    public void findBasketProductById_test() {
        Basket basket = new Basket();
        basket.setUserId(1L);
        basket.setBasketProducts(new ArrayList<>());
        basketRepository.save(basket);

        BasketProducts basketProducts = new BasketProducts(null, basket, 1L, "test", "author", 100.0, 1L);
        basketProductsRepository.save(basketProducts);

        BasketProducts foundBasketProducts = basketProductsRepository.findBasketProductById(basketProducts.getId());

        Assertions.assertEquals(foundBasketProducts.getQuantity(), 1);
    }

    private Basket newBasket() {
        String email = "customer@example.com";
        String password = "password";

        Customer customer = new Customer();
        customer.setName("customer");
        customer.setEmail(email);
        customer.setPassword(password);
        customer.setRole(Role.ADMIN);
        customer.setLastName("customer");
        customer.setNumber(123456);
        customerRepository.save(customer);

        Basket basket = new Basket();
        basket.setUserId(customer.getId());
        basket.setBasketProducts(new ArrayList<>());
        basketRepository.save(basket);

        BookCategory bookCategory = new BookCategory(null, "bookCategory");
        bookCategoryRepository.save(bookCategory);
        Book book = new Book();
        book.setBookCategory(bookCategory);
        book.setTitle("title");
        book.setAuthor("test");
        book.setPrice(100.0);
        book.setQuantity(1L);
        book.setStatus(Status.AVAILABLE);
        bookRepository.save(book);

        BasketProducts basketProducts = new BasketProducts();
        basketProducts.setName("test");
        basketProducts.setBasket(basket);
        basketProducts.setAuthor("author");
        basketProducts.setPrice(100.0);
        basketProducts.setIdBook(book.getId());
        basketProducts.setQuantity(1L);

        basket.getBasketProducts().add(basketProducts);
        return basketRepository.save(basket);
    }
}
