package com.example.Book_Store.controller;

import com.example.Book_Store.entities.*;

import com.example.Book_Store.service.implementation.BookServiceImpl;
import com.example.Book_Store.service.implementation.CategoryServiceImpl;
import com.example.Book_Store.service.implementation.CustomerServiceImpl;
import com.example.Book_Store.service.implementation.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
        try {
            Category createCategory = categoryService.createCategory(category);
            logger.info("Created a new note with ID {}.", createCategory.getId());
            return new ResponseEntity<>(createCategory, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Error while creating a note: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getCategory/id/{id}")
    public ResponseEntity<?> getCategoryById(@PathVariable Integer id) {
        try {
            Optional<Category> getCategoryOptional = categoryService.findCategoryById(id);
            if (getCategoryOptional.isPresent()) {
                Category getCategory = getCategoryOptional.get();
                logger.info("Category {} is present", getCategory);
                return new ResponseEntity<>(getCategory, HttpStatus.OK);
            } else {
                logger.warn("Category with this ID not exists.");
                return new ResponseEntity<>("Category not exists", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("Error while display a category: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateCategory/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @RequestBody Category category) {
        try {
            Optional<Category> getCategoryOptional = categoryService.findCategoryById(id);
            if (getCategoryOptional.isPresent()) {
                Category existingCategory = getCategoryOptional.get();
                existingCategory.setName(category.getName());
                Category updateCategory = categoryService.updateCategory(existingCategory);
                logger.info("Category with ID {} has been updated.", id);
                return new ResponseEntity<>(updateCategory, HttpStatus.OK);
            } else {
                logger.warn("Category with ID {} does not exist.", id);

                return new ResponseEntity<>("Category not exists", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error while updating a category: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/deleteCategories")
    public ResponseEntity<?> deleteAllCategories() {
        try {
            List<Category> allCategories = categoryService.getAllCategories();
            if (allCategories.isEmpty()) {
                logger.warn("Categories list is empty. No category to delete.");
                return new ResponseEntity<>("Categories list is empty", HttpStatus.NOT_FOUND);
            } else {
                categoryService.deleteAllCategories();
                logger.info("All Categories have been deleted.");
                return new ResponseEntity<>("All categories have been deleted", HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Error while deleting a categories: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteCategory/id/{idCategory}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Integer idCategory) {
        try {
            if (categoryService.categoryExistsById(idCategory)) {
                categoryService.deleteCategoryById(idCategory);

                logger.info("Category with ID {} has been deleted.", idCategory);
                return new ResponseEntity<>("Category deleted", HttpStatus.OK);
            } else {
                logger.warn("Category with ID {} does not exist.", idCategory);
                return new ResponseEntity<>("Category not exists", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error while deleting a category: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addBook")
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        try {
            Book addBook = bookService.createBook(book);
            logger.info("Added a new book with ID {}.", addBook.getId());
            return new ResponseEntity<>(addBook, HttpStatus.CREATED);

        } catch (Exception e) {
            logger.error("Error while add a book: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteBook/id/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable Integer id) {
        try {

            if (bookService.existsBookById(id)) {
                bookService.deleteBookById(id);
                logger.info("Book with ID {} has been deleted.", id);
                return new ResponseEntity<>("Book deleted", HttpStatus.OK);
            } else {
                logger.warn("Book with ID {} does not exist.", id);
                return new ResponseEntity<>("Book not exists", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error while deleting a book: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteBook/title/{title}")
    public ResponseEntity<?> deleteBookByTitle(@PathVariable String title) {
        try {
            if (bookService.existsBookByTitle(title)) {
                bookService.deleteBookByTitle(title);
                logger.info("Book {} has been deleted.", title);
                return new ResponseEntity<>("Book deleted", HttpStatus.OK);
            } else {
                logger.warn("Book {} not exist.", title);
                return new ResponseEntity<>("Book not exists", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error while deleting a book: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/deleteBooks")
    public ResponseEntity<?> deleteAllBooks() {
        try {
            List<Book> books = bookService.findAllBooks();
            if (!books.isEmpty()) {
                bookService.deleteAllBooks();
                logger.info("All Books has been deleted.");
                return new ResponseEntity<>("Books deleted", HttpStatus.OK);
            } else {
                logger.warn("Book not exist.");
                return new ResponseEntity<>("Book not exists", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error while deleting a books: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/updateBook/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Integer id, @RequestBody Book book) {
        try {
            if (bookService.existsBookById(id)) {
                Book existingBook = bookService.findBookById(id);
                existingBook.setCategory(book.getCategory());
                existingBook.setTitle(book.getTitle());
                existingBook.setAuthor(book.getAuthor());
                existingBook.setPrice(book.getPrice());
                existingBook.setQuantity(book.getQuantity());
                Book updateBook = bookService.updateBook(existingBook);
                logger.info("Book with ID {} has been updated.", id);
                return new ResponseEntity<>(updateBook, HttpStatus.OK);
            } else {
                logger.warn("Book with ID {} does not exist.", id);

                return new ResponseEntity<>("Book not exists", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("Error while updating a book: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getCustomers")
    public ResponseEntity<?> getAllCustomers() {
        try {
            List<Customer> customers = customerService.findAllCustomers();
            if (customers.isEmpty()) {
                logger.info("Customer list is empty");
                return new ResponseEntity<>("Customer list is empty", HttpStatus.NOT_FOUND);
            } else {

                logger.info("List customers exists");
                return new ResponseEntity<>(customers, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Error while display a customers: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getOrders")
    public ResponseEntity<?> getOrders() {
        try {
            List<Order> orders = orderService.findAllOrders();
            if (orders.isEmpty()) {
                logger.info("Order list is empty");
                return new ResponseEntity<>("Order list is empty", HttpStatus.NOT_FOUND);
            } else {
                logger.info("List orders exists");
                return new ResponseEntity<>(orders, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Error while display a Orders: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getOrder/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer id) {
        try {
            Optional<Order> getOrderOptional = orderService.findOrderById(id);
            if (getOrderOptional.isPresent()) {
                Order getOrder = getOrderOptional.get();
                logger.info("Category {} is present", getOrder);
                return new ResponseEntity<>(getOrder, HttpStatus.OK);
            } else {
                logger.warn("Category with this ID not exists.");
                return new ResponseEntity<>("Category not exists", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("Error while display a category: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

   /* @PutMapping("/orders/send/{id}")
    public ResponseEntity<?> sendOrder(@PathVariable Integer id) {
        try {
            if (orderService.orderExistsById(id)) {
                Order sendOrder = orderService.sendOrder(id);
                logger.info("Order with this ID {} has been sent", sendOrder);
                return new ResponseEntity<>(sendOrder, HttpStatus.OK);
            } else {
                logger.warn("Order with this ID has not been sent .");
                return new ResponseEntity<>("Order with this ID has not been sent", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("Error while sending a order: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    */

