package com.example.Book_Store.controller;

import com.example.Book_Store.entities.Book;
import com.example.Book_Store.entities.Category;
import com.example.Book_Store.entities.Order;
import com.example.Book_Store.service.implementation.BookServiceImpl;
import com.example.Book_Store.service.implementation.CategoryServiceImpl;
import com.example.Book_Store.service.implementation.CustomerServiceImpl;
import com.example.Book_Store.service.implementation.OrderServiceImpl;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdministratorController {
    private final BookServiceImpl bookService;
    private final CategoryServiceImpl categoryService;
    private final CustomerServiceImpl customerService;
    private final OrderServiceImpl orderService;
    private static final Logger logger = LoggerFactory.getLogger(AdministratorController.class);

    @Autowired
    public AdministratorController(BookServiceImpl bookService, CategoryServiceImpl categoryService,
                                   CustomerServiceImpl customerService, OrderServiceImpl orderService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @PostMapping("/addCategory")
    public ResponseEntity<?> createCategory(@RequestBody Category category) {

        Category createdCategory = categoryService.createCategory(category);
        logger.info("Created a new category with ID {}.", createdCategory.getId());
        return ResponseEntity.ok(createdCategory);
    }

    @GetMapping("/getCategory/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer id) {

        Category category = categoryService.findCategoryById(id);
        logger.info("Category {} is present. Category details: {}", id, category);
        return ResponseEntity.ok(category);
    }

    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody Category category) {

        Category updateCategory = categoryService.updateCategory(id, category);
        logger.info("Category with ID {} has been updated.", id);
        return ResponseEntity.ok(updateCategory);
    }

    @Transactional
    @DeleteMapping("/deleteCategories")
    public ResponseEntity<?> deleteAllCategories() {

        categoryService.deleteAllCategories();
        logger.info("All categories have been deleted.");
        return ResponseEntity.ok("All categories have been deleted");
    }

    @Transactional
    @DeleteMapping("/deleteCategory/{idCategory}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer idCategory) {

        categoryService.deleteCategoryById(idCategory);
        logger.info("Category with ID {} has been deleted.", idCategory);
        return ResponseEntity.ok("Category deleted");
    }

    @PostMapping("/addBook")
    public ResponseEntity<?> addBook(@RequestBody Book book) {

        Book addBook = bookService.createBook(book);
        logger.info("Added a new book with ID {}.", addBook.getId());
        return ResponseEntity.ok(addBook);
    }

    @Transactional
    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable Integer id) {

        bookService.deleteBookById(id);
        logger.info("Book with ID {} has been deleted.", id);
        return ResponseEntity.ok("Book deleted");
    }

    @DeleteMapping("/deleteBook/title/{title}")
    public ResponseEntity<?> deleteBookByTitle(@PathVariable String title) {

        bookService.deleteBookByTitle(title);
        logger.info("Book {} has been deleted.", title);
        return ResponseEntity.ok("Book deleted");
    }

    @DeleteMapping("/deleteBooks")
    public ResponseEntity<?> deleteAllBooks() {

        bookService.deleteAllBooks();
        logger.info("All Books has been deleted.");
        return ResponseEntity.ok("Books deleted");
    }

    @PutMapping("/updateBook/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Integer id, @RequestBody Book book) {

        Book updateBook = bookService.updateBook(id, book);
        logger.info("Book with ID {} has been updated.", id);
        return ResponseEntity.ok(updateBook);
    }

    @GetMapping("/getCustomers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {

        List<CustomerDTO> customers = customerService.findAllCustomers();
        logger.info("List customers exists");
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/getOrders")
    public ResponseEntity<List<OrderDTO>> getOrders() {

        List<OrderDTO> orders = orderService.findAllOrders();
        logger.info("List orders exists");
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/getOrder/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Integer id) {

        OrderDTO orderDTO = orderService.findOrderById(id);
        logger.info("Order {} is present", orderDTO.getId());
        return ResponseEntity.ok(orderDTO);
    }

    @PutMapping("/sendOrders/{id}")
    public ResponseEntity<?> sendOrder(@PathVariable Integer id) {

        Order order = orderService.updateOrderStatus(id);
        logger.info("Order with this ID {} has been sent", order.getId());
        return ResponseEntity.ok(order.getStatus());
    }
}

