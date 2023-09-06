package com.example.Book_Store.service;

import com.example.Book_Store.entities.Category;

import java.util.List;

public interface CategoryService {
    Category findCategoryById(Long id);

    Category findCategoryByName(String name);

    void deleteCategoryById(Long id);

    Category createCategory(Category category);
    List<Category> getAllCategories();
    void deleteAllCategories();
    Category updateCategory(Category category);
}

