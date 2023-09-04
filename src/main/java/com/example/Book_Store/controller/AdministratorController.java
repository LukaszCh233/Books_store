package com.example.Book_Store.controller;

import com.example.Book_Store.entities.*;
import com.example.Book_Store.service.BookService;
import com.example.Book_Store.service.CategoryService;
import com.example.Book_Store.service.CustomerService;
import com.example.Book_Store.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class AdministratorController {
    private final BookService bookService;
    private final CategoryService categoryService;
    private final CustomerService customerService;
    private final OrderService orderService;
@Autowired
    public AdministratorController(BookService bookService, CategoryService categoryService,
                                   CustomerService customerService, OrderService orderService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.customerService = customerService;
        this.orderService = orderService;
    }
    @PostMapping("/categories")
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }
    @GetMapping("/categories/id/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.findCategoryById(id);
    }
    @GetMapping("/categories/name/{name}")
    public Category getCategoryByName(@PathVariable String name) {
        return categoryService.findCategoryByName(name);
    }

    @PutMapping("/categories")
    public Category updateCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }
    @DeleteMapping("/categories")
    public void deleteAllCategories() {
        categoryService.deleteAllCategories();
    }

    @DeleteMapping("/categories/{idCategory}")
    public void deleteCategoryById(@PathVariable Long idCategory) {
        categoryService.deleteCategoryById(idCategory);
    }
    @PostMapping("/books")
    public Book createBook(@RequestBody Book book) {
        return bookService.createBook(book);
    }

    @DeleteMapping("/books/id{id}")
    public void deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);
    }
    @DeleteMapping("/books/title{title}")
    public void deleteBookByTitle(@PathVariable String title) {
        bookService.deleteBookByTitle(title);
    }

    @PutMapping("/books")
    public Book updateBook(@RequestBody Book book) {
        return bookService.updateBook(book);
    }

    @GetMapping("/customer")
    public List<Customer> getAllCustomers() {
        return customerService.findAllCustomers();
    }

    @GetMapping("/orders")
    public List<Order> getOrders() {
        return orderService.findAllOrders();
    }

    @GetMapping("/orders/{id}")
    public Order getOrderById(Long id) {
        return orderService.findOrderById(id);
    }
    @PutMapping("/orders/send/{id}")
    public Order sendOrder(@PathVariable Long id) {
        Order selectedOrder = orderService.findOrderById(id);
        if (selectedOrder != null) {
            selectedOrder.setStatus(Status.SENT);
        } else {
            return null;
        }
        return orderService.sendOrder(selectedOrder);
    }
}
