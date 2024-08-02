package com.example.Book_Store.serviceTest;

import com.example.Book_Store.basket.dto.BasketDTO;
import com.example.Book_Store.basket.entity.Basket;
import com.example.Book_Store.basket.entity.BasketProducts;
import com.example.Book_Store.basket.repository.BasketProductsRepository;
import com.example.Book_Store.basket.repository.BasketRepository;
import com.example.Book_Store.basket.service.BasketService;
import com.example.Book_Store.book.entity.Book;
import com.example.Book_Store.book.entity.Category;
import com.example.Book_Store.book.repository.BookRepository;
import com.example.Book_Store.book.repository.CategoryRepository;
import com.example.Book_Store.enums.Role;
import com.example.Book_Store.enums.Status;
import com.example.Book_Store.order.repository.OrderRepository;
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
public class BasketServiceTest {
    @Autowired
    BasketService basketService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    BasketProductsRepository basketProductsRepository;
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
    public void shouldDisplayBasketWithYourBooksWhenAddBookToBasket_test() {
        newCustomer("customer@example.com");
        Book book = newBook("title");

        basketService.addBookToBasket(book.getId(), 1L, new TestPrincipal("customer@example.com"));

        BasketDTO basket = basketService.findBasketDTOByUserPrincipal(new TestPrincipal("customer@example.com"));

        Assertions.assertEquals(basket.basketProductsList().get(0).name(), "title");
    }

    @Test
    public void whenUpdateBasketProductQuantityPriceShouldBeUpdate_test() {
        Customer customer = newCustomer("customer@example.com");
        Basket basket = newBasket(10L, 10.0, customer);

        Basket updateQuantity = basketService.updateBasketProductQuantity(basket.getBasketProducts().get(0).getId(), 5L, new TestPrincipal("customer@example.com"));

        Assertions.assertEquals(5, updateQuantity.getBasketProducts().get(0).getQuantity());
        Assertions.assertEquals(50, updateQuantity.getTotalPrice());
    }

    private Book newBook(String title) {
        Category category = new Category(null, "category");
        categoryRepository.save(category);

        Book book = new Book();
        book.setCategory(category);
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
        customer.setCustomerLogin(new CustomerLogin(email, "password"));
        customer.setNumber(123456);
        customer.setRole(Role.CUSTOMER);

        return customerRepository.save(customer);
    }

    private Basket newBasket(Long quantity, double price, Customer customer) {
        Category category = new Category(null, "category");
        categoryRepository.save(category);

        Book book = new Book();
        book.setCategory(category);
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
