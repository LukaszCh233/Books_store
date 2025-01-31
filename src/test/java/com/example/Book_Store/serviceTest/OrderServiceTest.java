package com.example.Book_Store.serviceTest;

import com.example.Book_Store.store.basket.entity.Basket;
import com.example.Book_Store.store.basket.entity.BasketProducts;
import com.example.Book_Store.store.basket.repository.BasketRepository;
import com.example.Book_Store.store.book.entity.Book;
import com.example.Book_Store.store.bookCategory.entity.BookCategory;
import com.example.Book_Store.store.book.repository.BookRepository;
import com.example.Book_Store.store.bookCategory.repository.BookCategoryRepository;
import com.example.Book_Store.account.Role;
import com.example.Book_Store.store.Status;
import com.example.Book_Store.store.order.dto.OrderDTO;
import com.example.Book_Store.store.order.entity.Order;
import com.example.Book_Store.store.order.repository.OrderRepository;
import com.example.Book_Store.store.order.service.OrderService;
import com.example.Book_Store.account.customer.Customer;
import com.example.Book_Store.account.customer.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class OrderServiceTest {
    @Autowired
    OrderService orderService;
    @Autowired
    CustomerRepository customerRepository;
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookCategoryRepository bookCategoryRepository;
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
    public void whenCustomerPlacedOrderThisOrderShouldBeInOrdersList_test() {
        newBasket();

        orderService.saveOrder(new TestPrincipal("customer@example.com"));

        List<OrderDTO> orderDTOList = orderService.findAllOrders();

        Assertions.assertEquals(orderDTOList.size(), 1);
        Assertions.assertEquals(orderDTOList.get(0).status(), Status.ORDERED);
    }

    @Test
    public void whenUpdateOrderStatusToSentOrderShouldBeInSentOrdersList() {
       Order order = newOrder();

        orderService.updateOrderStatus(order.getId());

        List<OrderDTO> sentOrders = orderService.findAllSentOrders();

        Assertions.assertEquals(sentOrders.size(),1);
    }

    private void newBasket() {
        Customer customer = new Customer();
        customer.setName("name");
        customer.setLastName("lastName");
        customer.setEmail("customer@example.com");
        customer.setPassword("password");
        customer.setNumber(123456);
        customer.setRole(Role.CUSTOMER);
        customerRepository.save(customer);

        BookCategory bookCategory = new BookCategory(null, "bookCategory");
        bookCategoryRepository.save(bookCategory);

        Book book = new Book();
        book.setBookCategory(bookCategory);
        book.setTitle("title");
        book.setAuthor("test");
        book.setPrice(10);
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
        basketProducts.setQuantity(2L);

        basket.getBasketProducts().add(basketProducts);
        basketRepository.save(basket);
    }

    private Order newOrder() {
        Customer customer = new Customer();
        customer.setName("name");
        customer.setLastName("lastName");
        customer.setEmail("customer@example.com");
        customer.setPassword("password");
        customer.setNumber(123456);
        customer.setRole(Role.CUSTOMER);
        customerRepository.save(customer);

        Order order = new Order();
        order.setOrderData(LocalDate.now());
        order.setStatus(Status.ORDERED);
        order.setCustomer(customer);
        return orderRepository.save(order);
    }
}
