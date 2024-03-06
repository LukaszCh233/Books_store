package com.example.Book_Store.serviceTests;

import com.example.Book_Store.controller.OrderDTO;
import com.example.Book_Store.entities.*;
import com.example.Book_Store.exceptions.OperationNotAllowedException;
import com.example.Book_Store.repository.*;
import com.example.Book_Store.service.implementation.OrderServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderServiceTest {

    private final OrderServiceImpl orderService;
    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final BasketRepository basketRepository;
    private final CustomerRepository customerRepository;
    private final BasketProductRepository basketProductRepository;

    @Autowired
    public OrderServiceTest(OrderServiceImpl orderService, BookRepository bookRepository, OrderRepository orderRepository,
                            BasketRepository basketRepository, CustomerRepository customerRepository,
                            BasketProductRepository basketProductRepository) {
        this.orderService = orderService;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.basketRepository = basketRepository;
        this.customerRepository = customerRepository;
        this.basketProductRepository = basketProductRepository;
    }

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldFindOrderById_ExistingOrder_Test() {
        //Given
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(null, "TestName", "TestLastName", customerLogin, 123456, null);

        OrderedBooks orderedBooks = new OrderedBooks();
        Order order = new Order(null, customer, null, 100.0, Status.ORDERED, Collections.singletonList(orderedBooks));

        customerRepository.save(customer);
        orderRepository.save(order);

        //When
        OrderDTO findOrder = orderService.findOrderById(order.getId());

        //Then
        assertEquals(findOrder.getId(), order.getId());
        assertEquals(findOrder.getPrice(), order.getPrice());
        assertEquals(findOrder.getStatus(), order.getStatus());
    }

    @Test
    void shouldFindOrderById_NotFound_Test() {
        int orderId = 1;

        assertThrows(EntityNotFoundException.class, () -> orderService.findOrderById(orderId));
    }

    @Test
    void shouldFindAllOrders_ExistingOrders_Test() {
        //Given
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(null, "TestName", "TestLastName", customerLogin, 123456, null);

        OrderedBooks orderedBooks = new OrderedBooks();
        List<Order> orders = Arrays.asList(
                new Order(null, customer, null, null, Status.ORDERED, Collections.singletonList(orderedBooks)),
                new Order(null, customer, null, null, Status.ORDERED, Collections.singletonList(orderedBooks)
                ));

        customerRepository.save(customer);
        orderRepository.saveAll(orders);

        //When
        List<OrderDTO> resultOrders = orderService.findAllOrders();

        //Then
        assertEquals(2, resultOrders.size());
        assertEquals(resultOrders.get(0).getStatus(), orders.get(0).getStatus());
        assertEquals(resultOrders.get(1).getStatus(), orders.get(1).getStatus());
        assertEquals(resultOrders.get(0).getId(), orders.get(0).getId());
        assertEquals(resultOrders.get(1).getId(), orders.get(1).getId());
    }

    @Test
    void shouldUpdateOrderStatus_Test() {
        //Given
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(null, "TestName", "TestLastName", customerLogin, 123456, null);

        OrderedBooks orderedBooks = new OrderedBooks();
        Order order = new Order(null, customer, null, null, Status.ORDERED, Collections.singletonList(orderedBooks));

        customerRepository.save(customer);
        orderRepository.save(order);

        //When
        Order updateOrder = orderService.updateOrderStatus(order.getId());

        //Then
        assertNotNull(updateOrder);
        assertEquals(Status.SENT, updateOrder.getStatus());
    }

    @Test
    void shouldUpdateOrderStatus_OrderSent_Test() {
        //Given
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(null, "TestName", "TestLastName", customerLogin, 123456, null);
        OrderedBooks orderedBooks = new OrderedBooks();
        Order order = new Order(null, customer, null, null, Status.SENT, Collections.singletonList(orderedBooks));

        customerRepository.save(customer);
        orderRepository.save(order);

        //When, Then
        assertThrows(OperationNotAllowedException.class, () -> orderService.updateOrderStatus(order.getId()));
    }

    @Test
    void shouldSaveOrder_Test() {
        //Given
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(null, "TestName", "TestLastName", customerLogin, 123456, null);

        customerRepository.save(customer);

        Book book = new Book(null, null, null, 10.0, 100, Status.AVAILABLE, null);

        bookRepository.save(book);

        Basket basket = new Basket(100.0, null, customer.getId(), null);

        basketRepository.save(basket);

        BasketProducts basketProducts = new BasketProducts(null, basket, book.getId(), "testName", "testAuthor", 100.0, 10);

        basketProductRepository.save(basketProducts);


        OrderedBooks orderedBooks = new OrderedBooks();
        Order order = new Order(null, customer, LocalDate.now(), 100.0, Status.ORDERED, Collections.singletonList(orderedBooks));

        orderRepository.save(order);

        //When
        Order savedOrder = orderService.saveOrder(order, new TestPrincipal(customerLogin.getEmail()));

        //Then
        assertNotNull(savedOrder);
        assertEquals(order.getCustomer().getId(), savedOrder.getCustomer().getId());
        assertEquals(order.getOrderData(), savedOrder.getOrderData());
        assertEquals(order.getPrice(), savedOrder.getPrice());
        assertEquals(order.getStatus(), savedOrder.getStatus());
        assertEquals(order.getOrderedBooks().size(), savedOrder.getOrderedBooks().size());
    }
}