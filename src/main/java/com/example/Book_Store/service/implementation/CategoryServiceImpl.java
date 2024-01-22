package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.Book;
import com.example.Book_Store.entities.Category;
import com.example.Book_Store.repository.BookRepository;
import com.example.Book_Store.repository.CategoryRepository;
import com.example.Book_Store.service.CategoryService;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository,BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Category findCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }


    @Override
    public Category findCategoryByName(String name) {
        return Optional.ofNullable(categoryRepository.findCategoryByName(name))
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public void deleteCategoryById(Integer id) {
        if (!categoryExistsById(id)) throw new ResourceNotFoundException("Category not found");
        List<Book> categoryBooks = bookRepository.findByCategoryId(id);
        if (!categoryBooks.isEmpty()) throw new ResourceNotFoundException("You cant delete category. Category contain books");
        categoryRepository.deleteCategoryById(id);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findAllCategories() {
        List<Category> categories = findAllCategories();
        if (categories.isEmpty()) {
            throw new ResourceNotFoundException("Category list is empty");
        }
        return categoryRepository.findAll();
    }

    @Override
    public void deleteAllCategories() {
        List<Category> allCategories = findAllCategories();
        if (allCategories.isEmpty()) {
            throw new ResourceNotFoundException("Category list is empty");
        }
        List<Book> booksInCategory = bookRepository.findAll();
        if (!booksInCategory.isEmpty()) {
            throw new ResourceNotFoundException("You cant delete categories, Categories contains books");
        }
        categoryRepository.deleteAll();
    }

    @Override
    public Category updateCategory(Integer id, Category updatedCategory) {
        Category presentCategory = findCategoryById(id);
        presentCategory.setName(updatedCategory.getName());
        return categoryRepository.save(presentCategory);
    }

    @Override
    public boolean categoryExistsById(Integer id) {
        return categoryRepository.existsById(id);
    }

}
