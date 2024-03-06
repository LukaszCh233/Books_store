package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.Book;
import com.example.Book_Store.entities.Category;
import com.example.Book_Store.exceptions.OperationNotAllowedException;
import com.example.Book_Store.repository.BookRepository;
import com.example.Book_Store.repository.CategoryRepository;
import com.example.Book_Store.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Category findCategoryById(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }


    @Override
    public Category findCategoryByName(String name) {
        return Optional.ofNullable(categoryRepository.findCategoryByName(name))
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
    }

    @Override
    public void deleteCategoryById(Integer id) {
        findCategoryById(id);
        List<Book> categoryBooks = bookRepository.findByCategoryId(id);
        if (!categoryBooks.isEmpty())
            throw new OperationNotAllowedException("You can not delete category. Category contain books");
        categoryRepository.deleteById(id);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new EntityNotFoundException("Category list is empty");
        }
        return categories;
    }

    @Override
    public void deleteAllCategories() {
        findAllCategories();
        List<Book> booksInCategory = bookRepository.findAll();
        if (!booksInCategory.isEmpty()) {
            throw new OperationNotAllowedException("You cant delete categories. Categories contains books");
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
