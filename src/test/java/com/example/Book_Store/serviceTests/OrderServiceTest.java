package com.example.Book_Store.serviceTests;

import com.example.Book_Store.controller.OrderDTO;
import com.example.Book_Store.entities.*;
import com.example.Book_Store.exceptions.OperationNotAllowedException;
import com.example.Book_Store.repository.*;
import com.example.Book_Store.service.implementation.BasketServiceImpl;
import com.example.Book_Store.service.implementation.BookServiceImpl;
import com.example.Book_Store.service.implementation.OrderServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderServiceTest {

    @Mock
    OrderedBooksRepository orderedBooksRepository;
    @InjectMocks
    OrderServiceImpl orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private BasketRepository basketRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BasketServiceImpl basketService;
    @Mock
    private BasketProductRepository basketProductRepository;
    @Mock
    private BookServiceImpl bookService;

    @Test
    void shouldFindOrderById_ExistingOrder_Test() {
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(1, "TestName", "TestLastName", customerLogin, 123456, null);
        OrderedBooks orderedBooks = new OrderedBooks();
        Order order = new Order(1, customer, null, null, Status.ORDERED, Collections.singletonList(orderedBooks));

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        OrderDTO findOrder = orderService.findOrderById(order.getId());

        assertEquals(findOrder.getId(), order.getId());

        verify(orderRepository, times(1)).findById(order.getId());
    }

    @Test
    void shouldFindOrderById_NotFound_Test() {
        int orderId = 1;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.findOrderById(orderId));

        verify(orderRepository, times(1)).findById(orderId);
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldFindAllOrders_ExistingOrders_Test() {
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(1, "TestName", "TestLastName", customerLogin, 123456, null);

        OrderedBooks orderedBooks = new OrderedBooks();
        List<Order> mockOrders = Arrays.asList(
                new Order(1, customer, null, null, Status.ORDERED, Collections.singletonList(orderedBooks)),
                new Order(2, customer, null, null, Status.ORDERED, Collections.singletonList(orderedBooks)
                ));

        when(orderRepository.findAll()).thenReturn(mockOrders);

        List<OrderDTO> expectedOrderDTOs = mockOrders.stream()
                .map(orderService::mapOrderToOrderDTO)
                .toList();

        List<OrderDTO> resultOrders = orderService.findAllOrders();

        verify(orderRepository, times(1)).findAll();

        assertIterableEquals(expectedOrderDTOs, resultOrders);
    }

    @Test
    void shouldFindAllOrders_NotExistingOrders_Test() {

        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> orderService.findAllOrders());

        verify(orderRepository, times(1)).findAll();
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void shouldUpdateOrderStatus_Test() {
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(1, "TestName", "TestLastName", customerLogin, 123456, null);
        OrderedBooks orderedBooks = new OrderedBooks();
        Order order = new Order(1, customer, null, null, Status.ORDERED, Collections.singletonList(orderedBooks));

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        Order updateOrder = orderService.updateOrderStatus(order.getId());

        assertNotNull(updateOrder);
        assertEquals(Status.SENT, updateOrder.getStatus());

        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(1)).save(order);

    }

    @Test
    void shouldUpdateOrderStatus_OrderSent_Test() {
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(1, "TestName", "TestLastName", customerLogin, 123456, null);
        OrderedBooks orderedBooks = new OrderedBooks();
        Order order = new Order(1, customer, null, null, Status.SENT, Collections.singletonList(orderedBooks));

        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        assertThrows(OperationNotAllowedException.class, () -> orderService.updateOrderStatus(order.getId()));

        verify(orderRepository, times(1)).findById(order.getId());
        verify(orderRepository, times(0)).save(order);
    }

    @Test
    void shouldSaveOrder_Test() {
        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(1, "TestName", "TestLastName", customerLogin, 123456, null);

        Principal principal = Mockito.mock(Principal.class);

        when(principal.getName()).thenReturn(customerLogin.getEmail());

        BasketProducts basketProducts = new BasketProducts(null, null, 1, "testName", "testAuthor", 100.0, 10);
        Basket basket = new Basket(100.0, 1, customer.getId(), Collections.singletonList(basketProducts));
        when(bookService.findBookById(basketProducts.getIdBook())).thenReturn(new Book());
        when(basketService.findBasketByUserPrincipal(principal)).thenReturn(basket);
        when(customerRepository.findById(customer.getId())).thenReturn(java.util.Optional.of(customer));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OrderedBooks orderedBooks = new OrderedBooks();
        Order order = new Order(1, customer, LocalDate.now(), 100.0, Status.ORDERED, Collections.singletonList(orderedBooks));

        Order savedOrder = orderService.saveOrder(order, principal);

        assertNotNull(savedOrder);
        assertEquals(order.getCustomer(), savedOrder.getCustomer());
        assertEquals(order.getOrderData(), savedOrder.getOrderData());
        assertEquals(order.getPrice(), savedOrder.getPrice());
        assertEquals(order.getStatus(), savedOrder.getStatus());
        assertEquals(order.getOrderedBooks().size(), savedOrder.getOrderedBooks().size());
    }
}