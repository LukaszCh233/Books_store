package com.example.Book_Store.book.controller;

import com.example.Book_Store.book.entity.Category;
import com.example.Book_Store.book.service.CategoryService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminCategoryController {
    private final CategoryService categoryService;

    public AdminCategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/category")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category) {
        Category createdCategory = categoryService.createCategory(category);

        return ResponseEntity.ok(createdCategory);
    }

    @PutMapping("/category/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @Valid @RequestBody Category category) {
        Category updateCategory = categoryService.updateCategory(id, category);

        return ResponseEntity.ok(updateCategory);
    }

    @Transactional
    @DeleteMapping("/categories")
    public ResponseEntity<?> deleteAllCategories() {
        categoryService.deleteAllCategories();

        return ResponseEntity.ok("All categories have been deleted");
    }

    @Transactional
    @DeleteMapping("/category/{idCategory}")
    public ResponseEntity<?> deleteCategoryById(@PathVariable Long idCategory) {
        categoryService.deleteCategoryById(idCategory);

        return ResponseEntity.ok("Category deleted");
    }
}
