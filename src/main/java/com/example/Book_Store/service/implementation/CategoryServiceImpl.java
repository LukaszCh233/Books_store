package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.Category;
import com.example.Book_Store.repository.CategoryRepository;
import com.example.Book_Store.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Optional<Category> findCategoryById(Integer id) {
        return categoryRepository.findCategoryById(id);
    }

    @Override
    public Optional<Category> findCategoryByName(String name) {
        return categoryRepository.findCategoryByName(name);
    }

    @Override
    public void deleteCategoryById(Integer id) {
    categoryRepository.deleteCategoryById(id);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category) ;
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

    @Override
    public boolean categoryExistsById(Integer id) {
        return categoryRepository.existsById(id);
    }

}
