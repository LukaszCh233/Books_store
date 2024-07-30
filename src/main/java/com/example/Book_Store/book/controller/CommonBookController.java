package com.example.Book_Store.book.controller;

import com.example.Book_Store.book.entity.Book;
import com.example.Book_Store.book.service.BookServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/common")
public class CommonBookController {
    private final BookServiceImpl bookService;

    public CommonBookController(BookServiceImpl bookService) {
        this.bookService = bookService;
    }
    @GetMapping("/books")
    public ResponseEntity<List<Book>> displayAllBooks() {
        List<Book> books = bookService.findAllBooks();

        return ResponseEntity.ok(books);
    }
    @GetMapping("/book/title/{title}")
    public ResponseEntity<List<Book>> displayBookByTitle(@PathVariable String title) {
        List<Book> findBooks = bookService.findByTitle(title);

        return ResponseEntity.ok(findBooks);
    }
    @GetMapping("/book/id/{id}")
    public ResponseEntity<Book> displayBookById(@PathVariable Long id) {
        Book findBook = bookService.findBookById(id);

        return ResponseEntity.ok(findBook);
    }
    @GetMapping("/books/category-name/{name}")
    public ResponseEntity<List<Book>> displayBooksByCategoryName(@PathVariable String name) {
        List<Book> books = bookService.findByCategoryName(name);

        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/category/{id}")
    public ResponseEntity<List<Book>> displayBooksByCategoryId(@PathVariable Integer id) {
        List<Book> books = bookService.findByCategoryId(id);

        return ResponseEntity.ok(books);
    }
}
