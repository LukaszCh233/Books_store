package com.example.Book_Store.store.book.controller;

import com.example.Book_Store.store.book.dto.BookDTO;
import com.example.Book_Store.store.book.input.BookRequest;
import com.example.Book_Store.store.book.service.BookService;
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
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookRequest bookRequest) {
        BookDTO book = bookService.createBook(bookRequest);

        return ResponseEntity.ok(book);
    }

    @Transactional
    @DeleteMapping("/book/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable Long id) {
        bookService.deleteBookById(id);

        return ResponseEntity.ok("Book has been deleted");
    }

    @DeleteMapping("/books")
    public ResponseEntity<String> deleteAllBooks() {
        bookService.deleteAllBooks();

        return ResponseEntity.ok("Books has been deleted");
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookRequest bookRequest) {
        BookDTO updateBook = bookService.updateBook(id, bookRequest);

        return ResponseEntity.ok(updateBook);
    }
}
