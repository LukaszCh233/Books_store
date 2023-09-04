package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.Category;
import com.example.Book_Store.repository.CategoryRepository;
import com.example.Book_Store.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category findCategoryById(Long id) {
        return categoryRepository.findCategoryById(id);
    }

    @Override
    public Category findCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name);
    }

    @Override
    public void deleteCategoryById(Long id) {
    categoryRepository.deleteCategoryById(id);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void deleteAllCategories() {
    categoryRepository.deleteAll();
    }

    @Override
    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }
}
