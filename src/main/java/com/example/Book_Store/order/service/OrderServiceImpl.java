package com.example.Book_Store.order.service;

import com.example.Book_Store.basket.entity.Basket;
import com.example.Book_Store.basket.entity.BasketProducts;
import com.example.Book_Store.basket.service.BasketServiceImpl;
import com.example.Book_Store.book.entity.Book;
import com.example.Book_Store.book.service.BookServiceImpl;
import com.example.Book_Store.user.entity.Customer;
import com.example.Book_Store.user.repository.CustomerRepository;
import com.example.Book_Store.enums.Status;
import com.example.Book_Store.exceptions.OperationNotAllowedException;
import com.example.Book_Store.mapper.MapperEntity;
import com.example.Book_Store.order.dto.OrderDTO;
import com.example.Book_Store.order.entity.Order;
import com.example.Book_Store.order.entity.OrderedBooks;
import com.example.Book_Store.order.repository.OrderRepository;
import com.example.Book_Store.order.repository.OrderedBooksRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl {

    private final OrderRepository orderRepository;
    private final BasketServiceImpl basketService;
    private final CustomerRepository customerRepository;
    private final OrderedBooksRepository orderedBooksRepository;
    private final BookServiceImpl bookService;
    private final MapperEntity mapperEntity;

    public OrderServiceImpl(OrderRepository orderRepository, BasketServiceImpl basketService,
                            CustomerRepository customerRepository, OrderedBooksRepository orderedBooksRepository,
                            BookServiceImpl bookService, MapperEntity mapperEntity) {
        this.orderRepository = orderRepository;
        this.basketService = basketService;
        this.customerRepository = customerRepository;
        this.orderedBooksRepository = orderedBooksRepository;
        this.bookService = bookService;
        this.mapperEntity = mapperEntity;
    }

    public OrderDTO findOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return mapperEntity.mapOrderToOrderDTO(order);
    }

    public List<OrderDTO> findAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new EntityNotFoundException("Not found orders");
        }
        return mapperEntity.mapOrdersToOrdersDTO(orders);
    }


    public Order updateOrderStatus(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() == Status.SENT) {
            throw new OperationNotAllowedException("Order with this ID has already been sent");
        }
        order.setStatus(Status.SENT);
        return orderRepository.save(order);
    }


    public void saveOrder(Order order, Principal principal) {
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

            Long newQuantity = book.getQuantity() - orderedBooks.getQuantity();
            book.setQuantity(newQuantity);
            bookService.updateBook(book.getId(), book);
        }
        basketService.deleteBasketByPrincipal(principal);

        orderRepository.save(order);
    }
}
