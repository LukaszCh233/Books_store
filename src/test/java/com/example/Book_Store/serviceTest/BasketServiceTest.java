package com.example.Book_Store.serviceTest;

import com.example.Book_Store.store.basket.dto.BasketDTO;
import com.example.Book_Store.store.basket.entity.Basket;
import com.example.Book_Store.store.basket.entity.BasketProducts;
import com.example.Book_Store.store.basket.repository.BasketRepository;
import com.example.Book_Store.store.basket.service.BasketService;
import com.example.Book_Store.store.book.entity.Book;
import com.example.Book_Store.store.bookCategory.entity.BookCategory;
import com.example.Book_Store.store.book.repository.BookRepository;
import com.example.Book_Store.store.bookCategory.repository.BookCategoryRepository;
import com.example.Book_Store.account.Role;
import com.example.Book_Store.store.Status;
import com.example.Book_Store.store.order.repository.OrderRepository;
import com.example.Book_Store.account.customer.Customer;
import com.example.Book_Store.account.customer.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;

@SpringBootTest
@ActiveProfiles("test")
public class BasketServiceTest {
    @Autowired
    BasketService basketService;
    @Autowired
    BookCategoryRepository bookCategoryRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();
        basketRepository.deleteAll();
        customerRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    public void shouldDisplayBasketWithYourBooksWhenAddBookToBasket() {
        newCustomer("customer@example.com");
        Book book = newBook("title");

        basketService.addBookToBasket(book.getId(), 1L, new TestPrincipal("customer@example.com"));

        BasketDTO basket = basketService.findBasketDTOByUserPrincipal(new TestPrincipal("customer@example.com"));

        Assertions.assertEquals(basket.basketProductsList().get(0).name(), "title");
    }

    @Test
    public void whenUpdateBasketProductQuantityPriceShouldBeUpdate() {
        Customer customer = newCustomer("customer@example.com");
        Basket basket = newBasket(10L, 10.0, customer);

        Basket updateQuantity = basketService.updateBasketProductQuantity(basket.getBasketProducts().get(0).getId(), 5L, new TestPrincipal("customer@example.com"));

        Assertions.assertEquals(5, updateQuantity.getBasketProducts().get(0).getQuantity());
        Assertions.assertEquals(50, updateQuantity.getTotalPrice());
    }

    @Test
    public void whenUpdateBasketProductQuantityIsZeroProductShouldBeDeletedFromBasket() {
        Customer customer = newCustomer("customer@example.com");
        Basket basket = newBasket(10L, 10.0, customer);

        Basket updateQuantity = basketService.updateBasketProductQuantity(basket.getBasketProducts().get(0).getId(), 0L, new TestPrincipal("customer@example.com"));

        Assertions.assertEquals(updateQuantity.getBasketProducts().size(), 0);
    }


    private Book newBook(String title) {
        BookCategory bookCategory = new BookCategory(null, "bookCategory");
        bookCategoryRepository.save(bookCategory);

        Book book = new Book();
        book.setBookCategory(bookCategory);
        book.setTitle(title);
        book.setAuthor("author");
        book.setPrice(10.0);
        book.setQuantity(100L);
        book.setStatus(Status.AVAILABLE);
        return bookRepository.save(book);
    }

    private Customer newCustomer(String email) {
        Customer customer = new Customer();
        customer.setName("name");
        customer.setLastName("lastName");
        customer.setEmail(email);
        customer.setPassword("password");
        customer.setNumber(123456);
        customer.setRole(Role.CUSTOMER);

        return customerRepository.save(customer);
    }

    private Basket newBasket(Long quantity, double price, Customer customer) {
        BookCategory bookCategory = new BookCategory(null, "bookCategory");
        bookCategoryRepository.save(bookCategory);

        Book book = new Book();
        book.setBookCategory(bookCategory);
        book.setTitle("title");
        book.setAuthor("test");
        book.setPrice(price);
        book.setQuantity(100L);
        book.setStatus(Status.AVAILABLE);
        bookRepository.save(book);

        Basket basket = new Basket();
        basket.setUserId(customer.getId());
        basket.setBasketProducts(new ArrayList<>());

        BasketProducts basketProducts = new BasketProducts();
        basketProducts.setName(book.getTitle());
        basketProducts.setBasket(basket);
        basketProducts.setAuthor(book.getAuthor());
        basketProducts.setPrice(book.getPrice());
        basketProducts.setIdBook(book.getId());
        basketProducts.setQuantity(quantity);

        basket.getBasketProducts().add(basketProducts);
        basket.setTotalPrice(basketProducts.getPrice() * quantity);

        return basketRepository.save(basket);
    }
}
