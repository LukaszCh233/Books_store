package com.example.Book_Store.service;

import com.example.Book_Store.entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Optional<Category> findCategoryById(Integer id);

    Optional<Category> findCategoryByName(String name);

    void deleteCategoryById(Integer id);

    Category createCategory(Category category);
    List<Category> getAllCategories();
    void deleteAllCategories();
    Category updateCategory(Category category);
    boolean categoryExistsById(Integer id);

}

