package com.example.Book_Store.controller;

import com.example.Book_Store.entities.Book;
import com.example.Book_Store.entities.Category;
import com.example.Book_Store.service.BookService;
import com.example.Book_Store.service.CategoryService;
import com.example.Book_Store.service.CustomerLoginService;
import com.example.Book_Store.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/common")

public class CommonController {
    private final BookService bookService;
    private final CategoryService categoryService;
    private final CustomerService customerService;
    private final CustomerLoginService customerLoginService;
    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);

    public CommonController(BookService bookService, CategoryService categoryService, CustomerService customerService,
                            CustomerLoginService customerLoginService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.customerService = customerService;
        this.customerLoginService = customerLoginService;
    }
    @GetMapping("/getCategories")
    public List<Category> displayAllCategories() {
        return categoryService.getAllCategories();
    }
    @GetMapping("/getCategory/name/{name}")
    public ResponseEntity<?> displayCategoryByName(@PathVariable String name) {
        try {
            Optional<Category> getCategoryOptional = categoryService.findCategoryByName(name);
            if (getCategoryOptional.isPresent()) {
                Category getCategory = getCategoryOptional.get();
                logger.info("Category {} is present", getCategory);
                return new ResponseEntity<>(getCategory, HttpStatus.OK);
            } else {
                logger.warn("Category with this name not exists.");
                return new ResponseEntity<>("Category not exists", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error("Error while display a category: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBooks")
    public ResponseEntity<?> displayAllBooks() {
        try {
            List<Book> books = bookService.findAllBooks();
            if (books.isEmpty()) {
                logger.info("Book list is empty");
                return new ResponseEntity<>("Book list is empty", HttpStatus.NOT_FOUND);
            } else {
                logger.info("Book list exists");
                return new ResponseEntity<>(books, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("Error while display a books: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBook/title/{title}")
    public ResponseEntity<?> displayBookByTitle(@PathVariable String title) {
        try {
            if (bookService.existsBookByTitle(title)) {
                Book findBook = bookService.findByTitle(title);
                logger.info("Book with title '{}' exists", title);
                return new ResponseEntity<>(findBook, HttpStatus.OK);
            } else {
                logger.warn("Book with title '{}' does not exist.", title);
                return new ResponseEntity<>("Book not exists", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("An error occurred while processing the request: {}", e.getMessage(), e);
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBook/id/{id}")
    public ResponseEntity<?> displayBookById(@PathVariable Integer id) {
        try {
            if (bookService.existsBookById(id)) {
                Book findBook = bookService.findBookById(id);

                logger.info("Book with ID '{}' exists", id);
                return new ResponseEntity<>(findBook, HttpStatus.OK);
            } else {
                logger.warn("Book with ID '{}' does not exist.", id);
                return new ResponseEntity<>("Book not exists", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("An error occurred while processing the request: {}", e.getMessage(), e);
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBooks/categoryName/{name}")
    public ResponseEntity<?> displayBooksByCategoryName(@PathVariable String name) {
        try {
            List<Book> books = bookService.findByCategoryName(name);
            if (!books.isEmpty()) {
                logger.info("Books exists in this category");
                return new ResponseEntity<>(books,HttpStatus.OK);
            } else {
                logger.info("Books do not exists in this category");
                return new ResponseEntity<>("There are no books in this category",HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("An error occurred while processing the request: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error while fetching books: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getBooks/categoryId/{id}")
    public ResponseEntity<?> displayBooksByCategoryId(@PathVariable Integer id) {
        try {
            List<Book> books = bookService.findByCategoryId(id);
            if (!books.isEmpty()) {
                logger.info("Books exists in this category");
                return new ResponseEntity<>(books,HttpStatus.OK);
            } else {
                logger.info("Books do not exists in this category");
                return new ResponseEntity<>("There are no books in this category",HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("An error occurred while processing the request: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error while fetching books: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//dosnt work
    @GetMapping("/getBooks/partialTitle/{partialTitle}")
    public ResponseEntity<?> displayBookByPartialTitle(String partialTitle) {

        try {
            List<Book> books = bookService.findByPartialTitle(partialTitle);
            if (!books.isEmpty()) {
                logger.info("Books exist with the partial title: {}", partialTitle);

                return new ResponseEntity<>(books,HttpStatus.OK);
            } else {
                logger.info("No books found with the partial title: {}", partialTitle);
                return new ResponseEntity<>("There are no books in this category",HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("An error occurred while processing the request: {}", e.getMessage(), e);
            return new ResponseEntity<>("Error while fetching books: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


