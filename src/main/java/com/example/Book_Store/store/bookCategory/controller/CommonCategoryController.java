package com.example.Book_Store.store.bookCategory.controller;

import com.example.Book_Store.store.bookCategory.dto.BookCategoryDTO;
import com.example.Book_Store.store.bookCategory.input.BookCategoryRequest;
import com.example.Book_Store.store.bookCategory.service.BookCategoryService;
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
public class CommonCategoryController {
    private final BookCategoryService bookCategoryService;

    public CommonCategoryController(BookCategoryService bookCategoryService) {
        this.bookCategoryService = bookCategoryService;
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<BookCategoryDTO> displayCategory(@PathVariable Long id) {
        BookCategoryDTO bookCategory = bookCategoryService.findCategoryById(id);

        return ResponseEntity.ok(bookCategory);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<BookCategoryDTO>> displayAllCategories() {
        List<BookCategoryDTO> categories = bookCategoryService.findAllCategories();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/category")
    public ResponseEntity<BookCategoryDTO> displayCategoryByName(@RequestBody @Valid BookCategoryRequest bookCategoryRequest) {
        BookCategoryDTO getBookCategory = bookCategoryService.findCategoryByName(bookCategoryRequest);

        return ResponseEntity.ok(getBookCategory);
    }
}
