package com.example.Book_Store.store.order.service;

import com.example.Book_Store.account.customer.Customer;
import com.example.Book_Store.account.customer.CustomerRepository;
import com.example.Book_Store.exceptions.OperationNotAllowedException;
import com.example.Book_Store.mapper.MapperEntity;
import com.example.Book_Store.store.Status;
import com.example.Book_Store.store.basket.entity.Basket;
import com.example.Book_Store.store.basket.service.BasketService;
import com.example.Book_Store.store.book.entity.Book;
import com.example.Book_Store.store.book.repository.BookRepository;
import com.example.Book_Store.store.order.dto.OrderDTO;
import com.example.Book_Store.store.order.entity.Order;
import com.example.Book_Store.store.order.entity.OrderedBooks;
import com.example.Book_Store.store.order.repository.OrderRepository;
import com.example.Book_Store.store.order.repository.OrderedBooksRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;
    private final BasketService basketService;
    private final CustomerRepository customerRepository;
    private final OrderedBooksRepository orderedBooksRepository;
    private final MapperEntity mapperEntity;

    public OrderService(OrderRepository orderRepository, BookRepository bookRepository, BasketService basketService,
                        CustomerRepository customerRepository, OrderedBooksRepository orderedBooksRepository,
                        MapperEntity mapperEntity) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.basketService = basketService;
        this.customerRepository = customerRepository;
        this.orderedBooksRepository = orderedBooksRepository;
        this.mapperEntity = mapperEntity;
    }

    public OrderDTO findOrderById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return mapperEntity.mapOrderToOrderDTO(order);
    }

    public List<OrderDTO> findAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            throw new EntityNotFoundException("Orders not found");
        }
        return mapperEntity.mapOrdersToOrdersDTO(orders);
    }

    public List<OrderDTO> findAllSentOrders() {
        List<Order> orders = orderRepository.findByStatus(Status.SENT);
        if (orders.isEmpty()) {
            throw new EntityNotFoundException("Orders not found");
        }
        return mapperEntity.mapOrdersToOrdersDTO(orders);
    }

    public void updateOrderStatus(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (order.getStatus() == Status.SENT) {
            throw new OperationNotAllowedException("Order with this ID has already been sent");
        }

        order.setStatus(Status.SENT);

        orderRepository.save(order);
    }

    public void saveOrder(Principal principal) {
        Basket basket = basketService.findBasketByUserPrincipal(principal);

        if (basket.getBasketProducts() == null || basket.getBasketProducts().isEmpty()) {
            throw new EntityNotFoundException("Basket is empty");
        }
        Customer customer = customerRepository.findById(basket.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderData(LocalDate.now());
        order.setOrderedBooks(new ArrayList<>());

        double totalPrice = basket.getBasketProducts().stream()
                .mapToDouble(bp -> bp.getPrice() * bp.getQuantity())
                .sum();
        totalPrice = Math.round(totalPrice * 100.0) / 100.0;
        basket.setTotalPrice(totalPrice);
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

        List<Book> booksToUpdate = new ArrayList<>();

        for (OrderedBooks orderedBooks : orderedBooksList) {
            Book book = bookRepository.findById(orderedBooks.getIdBook()).orElseThrow(() ->
                    new EntityNotFoundException("Book not found"));

            Long newQuantity = book.getQuantity() - orderedBooks.getQuantity();
            book.setQuantity(newQuantity);
            if (book.getQuantity() == 0) {
                book.setStatus(Status.LACK);
            }
            booksToUpdate.add(book);
        }
        bookRepository.saveAll(booksToUpdate);
        basketService.deleteBasketByPrincipal(principal);

        orderRepository.save(order);
    }
}
