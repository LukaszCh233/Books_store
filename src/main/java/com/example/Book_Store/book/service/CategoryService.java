package com.example.Book_Store.book.service;

import com.example.Book_Store.book.entity.Book;
import com.example.Book_Store.book.entity.Category;
import com.example.Book_Store.book.repository.BookRepository;
import com.example.Book_Store.book.repository.CategoryRepository;
import com.example.Book_Store.exceptions.ExistsException;
import com.example.Book_Store.exceptions.OperationNotAllowedException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public CategoryService(CategoryRepository categoryRepository, BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    public Category findCategoryByName(String name) {
        return categoryRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    public void deleteCategoryById(Long id) {
        findCategoryById(id);
        List<Book> categoryBooks = bookRepository.findByCategoryId(id);
        if (!categoryBooks.isEmpty())
            throw new OperationNotAllowedException("You can not delete category. Category contain books");
        categoryRepository.deleteById(id);
    }

    public Category createCategory(Category category) {
        Optional<Category> existingCategory = categoryRepository.findByNameIgnoreCase(category.getName());
        if (existingCategory.isPresent()) {
            throw new ExistsException("Category exists");
        }
        return categoryRepository.save(category);
    }

    public List<Category> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new EntityNotFoundException("Category list is empty");
        }
        return categories;
    }

    public void deleteAllCategories() {
        findAllCategories();
        List<Book> booksInCategory = bookRepository.findAll();
        if (!booksInCategory.isEmpty()) {
            throw new OperationNotAllowedException("You cant delete categories. Categories contains books");
        }
        categoryRepository.deleteAll();
    }

    public Category updateCategory(Long id, Category updatedCategory) {
        Category presentCategory = findCategoryById(id);

        presentCategory.setName(updatedCategory.getName());

        return categoryRepository.save(presentCategory);
    }
}
