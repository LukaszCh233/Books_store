package com.example.Book_Store.book.controller;

import com.example.Book_Store.book.entity.Category;
import com.example.Book_Store.book.service.CategoryServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/common")
public class CommonCategoryController {
    private final CategoryServiceImpl categoryService;

    public CommonCategoryController(CategoryServiceImpl categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/category/{id}")
    public ResponseEntity<Category> displayCategory(@PathVariable Integer id) {
        Category category = categoryService.findCategoryById(id);

        return ResponseEntity.ok(category);
    }
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> displayAllCategories() {
        List<Category> categories = categoryService.findAllCategories();

        return ResponseEntity.ok(categories);
    }
    @GetMapping("/category/{name}")
    public ResponseEntity<Category> displayCategoryByName(@PathVariable String name) {
        Category getCategory = categoryService.findCategoryByName(name);

        return ResponseEntity.ok(getCategory);
    }
}
