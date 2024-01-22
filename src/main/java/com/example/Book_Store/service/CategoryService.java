package com.example.Book_Store.service;

import com.example.Book_Store.entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    Category findCategoryById(Integer id);

    Category findCategoryByName(String name);

    void deleteCategoryById(Integer id);

    Category createCategory(Category category);
    List<Category> findAllCategories();
    void deleteAllCategories();
    Category updateCategory(Integer id, Category category);
    boolean categoryExistsById(Integer id);

}

