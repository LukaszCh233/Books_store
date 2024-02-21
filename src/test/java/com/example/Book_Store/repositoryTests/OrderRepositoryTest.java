package com.example.Book_Store.repositoryTests;

import com.example.Book_Store.entities.Order;
import com.example.Book_Store.repository.OrderRepository;
import com.example.Book_Store.repository.OrderedBooksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderedBooksRepository orderedBooksRepository;

    @BeforeEach
    public void setUp() {
        orderedBooksRepository.deleteAll();
        orderRepository.deleteAll();

    }

    @Test
    void shouldSaveOrder_Test() {
        //Given
        Order order = new Order(null, null, null, 10.0, null, null);

        //When
        orderRepository.save(order);

        //Then
        List<Order> orders = orderRepository.findAll();

        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(1, orders.size());
    }

    @Test
    void shouldFindOrderById_Test() {
        //Given
        Order order = new Order(null, null, null, 10.0, null, null);

        //When
        orderRepository.save(order);

        //Then
        Optional<Order> foundOrder = orderRepository.findById(order.getId());

        assertTrue(foundOrder.isPresent());
        assertEquals(order.getId(), foundOrder.get().getId());
    }

    @Test
    void shouldFindAllOrders_Test() {
        //Given
        Order order = new Order(null, null, null, 10.0, null, null);
        Order order1 = new Order(null, null, null, 10.0, null, null);

        //When
        orderRepository.save(order);
        orderRepository.save(order1);

        //Then
        List<Order> orders = orderRepository.findAll();

        assertNotNull(orders);
        assertFalse(orders.isEmpty());
        assertEquals(2, orders.size());
    }
}
