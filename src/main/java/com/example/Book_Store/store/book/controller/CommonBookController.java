package com.example.Book_Store.store.book.controller;

import com.example.Book_Store.store.book.dto.BookDTO;
import com.example.Book_Store.store.book.service.BookService;
import com.example.Book_Store.store.bookCategory.input.BookCategoryRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/common")
public class CommonBookController {
    private final BookService bookService;

    public CommonBookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public ResponseEntity<List<BookDTO>> displayAllBooks() {
        List<BookDTO> books = bookService.findAllBooks();

        return ResponseEntity.ok(books);
    }

    @GetMapping("/book/title/{title}")
    public ResponseEntity<List<BookDTO>> displayBookByTitle(@PathVariable String title) {
        List<BookDTO> findBooks = bookService.findByTitle(title);

        return ResponseEntity.ok(findBooks);
    }

    @GetMapping("/book/id/{id}")
    public ResponseEntity<BookDTO> displayBookById(@PathVariable Long id) {
        BookDTO findBook = bookService.findBookById(id);

        return ResponseEntity.ok(findBook);
    }

    @GetMapping("/books/category-name")
    public ResponseEntity<List<BookDTO>> displayBooksByCategoryName(@RequestBody @Valid BookCategoryRequest bookCategoryRequest) {
        List<BookDTO> books = bookService.findByCategoryName(bookCategoryRequest);

        return ResponseEntity.ok(books);
    }

    @GetMapping("/books/category/{id}")
    public ResponseEntity<List<BookDTO>> displayBooksByCategoryId(@PathVariable Long id) {
        List<BookDTO> books = bookService.findByCategoryId(id);

        return ResponseEntity.ok(books);
    }
}
