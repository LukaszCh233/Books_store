package com.example.Book_Store.serviceTests;

import com.example.Book_Store.entities.*;
import com.example.Book_Store.repository.*;
import com.example.Book_Store.service.implementation.BasketServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BasketServiceTest {

    private final OrderRepository orderRepository;
    private final BasketRepository basketRepository;
    private final BasketServiceImpl basketService;
    private final BookRepository bookRepository;
    private final CustomerRepository customerRepository;
    private final BasketProductRepository basketProductRepository;

    @Autowired
    public BasketServiceTest(OrderRepository orderRepository, BasketRepository basketRepository, BasketServiceImpl basketService,
                             BookRepository bookRepository, CustomerRepository customerRepository, BasketProductRepository basketProductRepository) {
        this.orderRepository = orderRepository;
        this.basketRepository = basketRepository;
        this.basketService = basketService;
        this.bookRepository = bookRepository;
        this.customerRepository = customerRepository;
        this.basketProductRepository = basketProductRepository;
    }

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();
        basketRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldAddBookToBasket_Successfully() {
        // Given
        CustomerLogin customerLogin = new CustomerLogin("testEm1ai", "testPassword");
        Customer customer = new Customer(null, "TestName", "TestLastName", customerLogin, 123456, null);

        Book book = new Book(null, "Test Book", "Test Author", 10.0, 10, Status.AVAILABLE, null);

        customerRepository.save(customer);
        bookRepository.save(book);

        // When
        basketService.addBookToBasket(book.getId(), 3, new TestPrincipal(customerLogin.getEmail()));

        // Then
        Basket basket = basketService.findBasketByUserPrincipal(new TestPrincipal(customerLogin.getEmail()));

        assertNotNull(basket);
        assertEquals(3, basket.getBasketProducts().get(0).getQuantity());
    }

    @Test
    void shouldFindBasketByUserPrincipal_Successfully() {
        //Given
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");

        Customer customer = new Customer(null, "TestName", "TestLastName", customerLogin, 123456, null);

        customerRepository.save(customer);

        Basket basket = new Basket(100.0, null, customer.getId(), null);

        basketRepository.save(basket);

        //When
        Basket resultBasket = basketService.findBasketByUserPrincipal(new TestPrincipal(customer.getCustomerLogin().getEmail()));

        //Then
        assertNotNull(resultBasket);
        assertEquals(basket.getUserId(), resultBasket.getUserId());
        assertEquals(basket.getIdBasket(), resultBasket.getIdBasket());
    }

    @Test
    void shouldDeleteBasketSuccessfully() {
        //Given
        String userEmail = "testEmail";

        Customer customer = new Customer(null, "TestName", "TestLastName", new CustomerLogin(userEmail, "testPassword"), 123456, null);

        customerRepository.save(customer);

        Basket basket = new Basket(100.0, null, customer.getId(), null);

        basketRepository.save(basket);

        //When
        basketService.deleteBasketByPrincipal(new TestPrincipal(customer.getCustomerLogin().getEmail()));

        //Then
        Basket foundBasket = basketRepository.findBasketByUserId(customer.getId());

        assertNull(foundBasket);
    }

    @Test
    void shouldUpdateBasketProduct_Test() {
        // Given
        Customer customer = new Customer(null, "TestName", "TestLastName", new CustomerLogin("userEmail", "testPassword"), 123456, null);

        customerRepository.save(customer);

        Book selectedBook = new Book(null, null, null, 10.0, 100, Status.AVAILABLE, null);

        bookRepository.save(selectedBook);

        Basket basket = new Basket(60.0, null, customer.getId(), null);

        basketRepository.save(basket);

        BasketProducts basketProducts = new BasketProducts(null, basket, selectedBook.getId(), null, null, 20.0, 3);

        basketProductRepository.save(basketProducts);

        // When
        basketService.updateBasket(basketProducts.getId(), 3, new TestPrincipal(customer.getCustomerLogin().getEmail()));

        // Then
        assertEquals(3, basketProducts.getQuantity());
        assertEquals(20.0, basketProducts.getPrice());
    }
}



