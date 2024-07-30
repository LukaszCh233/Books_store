package com.example.Book_Store.repositoryTest;

import com.example.Book_Store.basket.entity.Basket;
import com.example.Book_Store.basket.entity.BasketProducts;
import com.example.Book_Store.basket.repository.BasketProductsRepository;
import com.example.Book_Store.basket.repository.BasketRepository;
import com.example.Book_Store.book.entity.Book;
import com.example.Book_Store.book.repository.BookRepository;
import com.example.Book_Store.book.entity.Category;
import com.example.Book_Store.book.repository.CategoryRepository;
import com.example.Book_Store.enums.Role;
import com.example.Book_Store.enums.Status;
import com.example.Book_Store.user.entity.Customer;
import com.example.Book_Store.user.entity.CustomerLogin;
import com.example.Book_Store.user.repository.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

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
    CategoryRepository categoryRepository;
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

        Basket foundBasket = basketRepository.findBasketByUserId(expectedBasket.getUserId());

        Assertions.assertEquals(expectedBasket.getUserId(), foundBasket.getUserId(), "User IDs should match");
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
        CustomerLogin customerLogin = new CustomerLogin("customer@example.com", "password");

        Customer customer = new Customer();
        customer.setName("customer");
        customer.setCustomerLogin(customerLogin);
        customer.setRole(Role.ADMIN);
        customer.setLastName("customer");
        customer.setNumber(123456);
        customerRepository.save(customer);

        Basket basket = new Basket();
        basket.setUserId(customer.getId());
        basket.setBasketProducts(new ArrayList<>());
        basketRepository.save(basket);

        Category category = new Category(null, "category");
        categoryRepository.save(category);
        Book book = new Book();
        book.setCategory(category);
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
