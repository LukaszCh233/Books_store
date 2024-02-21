package com.example.Book_Store.service.implementation;

import com.example.Book_Store.controller.OrderDTO;
import com.example.Book_Store.entities.*;
import com.example.Book_Store.exceptions.OperationNotAllowedException;
import com.example.Book_Store.repository.CustomerRepository;
import com.example.Book_Store.repository.OrderRepository;
import com.example.Book_Store.repository.OrderedBooksRepository;
import com.example.Book_Store.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final BasketServiceImpl basketService;
    private final CustomerRepository customerRepository;
    private final OrderedBooksRepository orderedBooksRepository;
    private final BookServiceImpl bookService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, BasketServiceImpl basketService, CustomerRepository customerRepository,
                            OrderedBooksRepository orderedBooksRepository, BookServiceImpl bookService) {
        this.orderRepository = orderRepository;
        this.basketService = basketService;
        this.customerRepository = customerRepository;
        this.orderedBooksRepository = orderedBooksRepository;
        this.bookService = bookService;


    }

    @Override
    public OrderDTO findOrderById(Integer id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return mapOrderToOrderDTO(order);
    }

    public OrderDTO mapOrderToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setCustomer(order.getCustomer());
        orderDTO.setOrderData(order.getOrderData());
        orderDTO.setPrice(order.getPrice());
        orderDTO.setStatus(order.getStatus());
        orderDTO.setOrderedBooks(order.getOrderedBooks());
        return orderDTO;
    }

    @Override
    public List<OrderDTO> findAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new EntityNotFoundException("Orders list is empty");
        }

        return orders.stream()
                .map(this::mapOrderToOrderDTO)
                .toList();
    }

    @Override
    public Order updateOrderStatus(Integer id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() == Status.SENT) {
            throw new OperationNotAllowedException("Order with this ID has already been sent");
        }
        order.setStatus(Status.SENT);
        return orderRepository.save(order);
    }

    @Override
    public Order saveOrder(Order order, Principal principal) {
        Basket basket = basketService.findBasketByUserPrincipal(principal);

        if (basket.getBasketProducts() == null || basket.getBasketProducts().isEmpty()) {
            throw new EntityNotFoundException("Basket is empty");
        }
        Customer customer = customerRepository.findById(basket.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        order.setCustomer(customer);
        order.setOrderData(LocalDate.now());
        order.setOrderedBooks(new ArrayList<>());

        double totalPrice = basket.getBasketProducts().stream().mapToDouble(BasketProducts::getPrice)
                .sum();
        order.setPrice(totalPrice);
        orderRepository.save(order);

        List<OrderedBooks> orderedBooksList = basket.getBasketProducts().stream().map(basketProducts -> {
            OrderedBooks orderedBooks = new OrderedBooks();
            orderedBooks.setOrder(order);
            orderedBooks.setIdBook(basketProducts.getIdBook());
            orderedBooks.setQuantity(basketProducts.getQuantity());
            return orderedBooks;
        }).collect(Collectors.toList());

        orderedBooksRepository.saveAll(orderedBooksList);
        order.setOrderedBooks(orderedBooksList);
        order.setStatus(Status.ORDERED);

        for (OrderedBooks orderedBooks : orderedBooksList) {
            Book book = bookService.findBookById(orderedBooks.getIdBook());

            int newQuantity = book.getQuantity() - orderedBooks.getQuantity();
            book.setQuantity(newQuantity);
            bookService.updateBook(book.getId(), book);
        }
        basketService.deleteBasketById(principal);
        return orderRepository.save(order);
    }
}
