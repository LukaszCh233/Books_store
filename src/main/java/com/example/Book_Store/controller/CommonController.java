package com.example.Book_Store.controller;

import com.example.Book_Store.entities.Book;
import com.example.Book_Store.entities.Category;
import com.example.Book_Store.service.implementation.BookServiceImpl;
import com.example.Book_Store.service.implementation.CategoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/common")

public class CommonController {
    private static final Logger logger = LoggerFactory.getLogger(CommonController.class);
    private final BookServiceImpl bookService;
    private final CategoryServiceImpl categoryService;

    @Autowired
    public CommonController(BookServiceImpl bookService, CategoryServiceImpl categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;

    }

    @GetMapping("/getCategories")
    public ResponseEntity<List<Category>> displayAllCategories() {

        List<Category> categories = categoryService.findAllCategories();
        logger.info("Categories list {}", categories);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/getCategoryByName/{name}")
    public ResponseEntity<Category> displayCategoryByName(@PathVariable String name) {

        Category getCategory = categoryService.findCategoryByName(name);
        logger.info("Category {} is present", getCategory);
        return ResponseEntity.ok(getCategory);
    }

    @GetMapping("/getBooks")
    public ResponseEntity<List<Book>> displayAllBooks() {

        List<Book> books = bookService.findAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/getBook/title/{title}")
    public ResponseEntity<List<Book>> displayBookByTitle(@PathVariable String title) {

        List<Book> findBooks = bookService.findByTitle(title);

        logger.info("Book with title '{}' exists", title);
        return ResponseEntity.ok(findBooks);
    }

    @GetMapping("/getBook/id/{id}")
    public ResponseEntity<Book> displayBookById(@PathVariable Integer id) {

        Book findBook = bookService.findBookById(id);

        logger.info("Book with ID '{}' exists", id);
        return ResponseEntity.ok(findBook);
    }

    @GetMapping("/getBooks/categoryName/{name}")
    public ResponseEntity<List<Book>> displayBooksByCategoryName(@PathVariable String name) {

        List<Book> books = bookService.findByCategoryName(name);

        logger.info("Books exists in this category");
        return ResponseEntity.ok(books);
    }

    @GetMapping("/getBooks/categoryId/{id}")
    public ResponseEntity<List<Book>> displayBooksByCategoryId(@PathVariable Integer id) {

        List<Book> books = bookService.findByCategoryId(id);

        logger.info("Books exists in this category");
        return ResponseEntity.ok(books);
    }
}


