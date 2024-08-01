package com.example.Book_Store.book.controller;

import com.example.Book_Store.book.entity.Book;
import com.example.Book_Store.book.service.BookService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminBookController {
    private final BookService bookService;

    public AdminBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/book")
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book addBook = bookService.createBook(book);

        return ResponseEntity.ok(addBook);
    }

    @Transactional
    @DeleteMapping("/book/{id}")
    public ResponseEntity<?> deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);

        return ResponseEntity.ok("Book deleted");
    }

    @DeleteMapping("/book/title/{title}")
    public ResponseEntity<?> deleteBookByTitle(@PathVariable String title) {
        bookService.deleteBookByTitle(title);

        return ResponseEntity.ok("Book deleted");
    }

    @DeleteMapping("/books")
    public ResponseEntity<?> deleteAllBooks() {
        bookService.deleteAllBooks();

        return ResponseEntity.ok("Books deleted");
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        Book updateBook = bookService.updateBook(id, book);

        return ResponseEntity.ok(updateBook);
    }
}
