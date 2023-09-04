package com.example.Book_Store.controller;

import com.example.Book_Store.entities.Book;
import com.example.Book_Store.entities.Category;
import com.example.Book_Store.service.BookService;
import com.example.Book_Store.service.CategoryService;
import com.example.Book_Store.service.CustomerLoginService;
import com.example.Book_Store.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")

public class CommonController {
    private final BookService bookService;
    private final CategoryService categoryService;
    private final CustomerService customerService;
    private final CustomerLoginService customerLoginService;

    public CommonController(BookService bookService, CategoryService categoryService, CustomerService customerService,
                            CustomerLoginService customerLoginService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
        this.customerService = customerService;
        this.customerLoginService = customerLoginService;
    }
    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.findAllBooks();
    }

    @GetMapping("/books/title/{title}")
    public Book getBookByTitle(@PathVariable String title) {
        return bookService.findByTitle(title);
    }

    @GetMapping("/books/id/{id}")
    public Book getBookById(@PathVariable Long id) {
        return bookService.findBookById(id);
    }

    @GetMapping("/books/categoryName/{name}")
    public List<Book> getBooksByCategoryName(@PathVariable String name) {
        return bookService.findByCategoryName(name);
    }

    @GetMapping("/books/categoryId/{id}")
    public List<Book> getBooksByCategoryId(@PathVariable long id) {
        return bookService.findByCategoryId(id);
    }

    @GetMapping("/books/partialTitle/{partialTitle}")
    public List<Book> getBookByPartialTitle(String partialTitle) {
        return bookService.findByPartialTitle(partialTitle);
    }
}


