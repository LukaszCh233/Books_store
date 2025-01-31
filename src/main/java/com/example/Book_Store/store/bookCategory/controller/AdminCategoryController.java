package com.example.Book_Store.store.bookCategory.controller;

import com.example.Book_Store.store.bookCategory.dto.BookCategoryDTO;
import com.example.Book_Store.store.bookCategory.entity.BookCategory;
import com.example.Book_Store.store.bookCategory.input.BookCategoryRequest;
import com.example.Book_Store.store.bookCategory.service.BookCategoryService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminCategoryController {
    private final BookCategoryService bookCategoryService;

    public AdminCategoryController(BookCategoryService bookCategoryService) {
        this.bookCategoryService = bookCategoryService;
    }

    @PostMapping("/bookCategory")
    public ResponseEntity<BookCategory> createCategory(@Valid @RequestBody BookCategoryRequest bookCategoryRequest) {
        BookCategory createdBookCategory = bookCategoryService.createCategory(bookCategoryRequest);

        return ResponseEntity.ok(createdBookCategory);
    }

    @PutMapping("/bookCategory/{id}")
    public ResponseEntity<BookCategoryDTO> updateCategory(@PathVariable Long id,
                                                          @RequestBody @Valid BookCategoryRequest bookCategoryRequest) {
        BookCategoryDTO updateBookCategory = bookCategoryService.updateCategory(id, bookCategoryRequest);

        return ResponseEntity.ok(updateBookCategory);
    }

    @Transactional
    @DeleteMapping("/categories")
    public ResponseEntity<String> deleteAllCategories() {
        bookCategoryService.deleteAllCategories();

        return ResponseEntity.ok("All categories have been deleted");
    }

    @Transactional
    @DeleteMapping("/category/{idCategory}")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long idCategory) {
        bookCategoryService.deleteCategoryById(idCategory);

        return ResponseEntity.ok("Book category deleted");
    }
}
