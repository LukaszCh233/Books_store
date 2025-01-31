package com.example.Book_Store.store.bookCategory.service;

import com.example.Book_Store.exceptions.ExistsException;
import com.example.Book_Store.exceptions.OperationNotAllowedException;
import com.example.Book_Store.mapper.MapperEntity;
import com.example.Book_Store.store.book.repository.BookRepository;
import com.example.Book_Store.store.bookCategory.dto.BookCategoryDTO;
import com.example.Book_Store.store.bookCategory.entity.BookCategory;
import com.example.Book_Store.store.bookCategory.input.BookCategoryRequest;
import com.example.Book_Store.store.bookCategory.repository.BookCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookCategoryService {
    private final BookCategoryRepository bookCategoryRepository;
    private final BookRepository bookRepository;
    private final MapperEntity mapperEntity;

    public BookCategoryService(BookCategoryRepository bookCategoryRepository, BookRepository bookRepository,
                               MapperEntity mapperEntity) {
        this.bookCategoryRepository = bookCategoryRepository;
        this.bookRepository = bookRepository;
        this.mapperEntity = mapperEntity;
    }

    public BookCategoryDTO findCategoryById(Long id) {
        BookCategory bookCategory = bookCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book category not found"));

        return mapperEntity.mapBookCategoryToBookCategoryDTO(bookCategory);
    }

    public BookCategoryDTO findCategoryByName(BookCategoryRequest bookCategoryRequest) {
        BookCategory bookCategory = bookCategoryRepository.findByNameIgnoreCase(bookCategoryRequest.getName())
                .orElseThrow(() -> new EntityNotFoundException("Book category not found"));

        return mapperEntity.mapBookCategoryToBookCategoryDTO(bookCategory);
    }

    public void deleteCategoryById(Long id) {
        BookCategory bookCategory = bookCategoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Category not found"));
        if (bookRepository.existsByBookCategoryId(id)) {
            throw new OperationNotAllowedException("You can not delete category. Book category contain books");
        }
        bookCategoryRepository.delete(bookCategory);
    }

    public BookCategory createCategory(BookCategoryRequest bookCategoryRequest) {
        Optional<BookCategory> existingCategory = bookCategoryRepository.findByNameIgnoreCase(bookCategoryRequest.getName());
        if (existingCategory.isPresent()) {
            throw new ExistsException("Book category exists");
        }
        BookCategory bookCategory = new BookCategory();
        bookCategory.setName(bookCategoryRequest.getName());

        return bookCategoryRepository.save(bookCategory);
    }

    public List<BookCategoryDTO> findAllCategories() {
        List<BookCategory> categories = bookCategoryRepository.findAll();
        if (categories.isEmpty()) {
            throw new EntityNotFoundException("Book category list is empty");
        }
        return mapperEntity.mapBookCategoryListToBookCategoryListDTO(categories);
    }

    public void deleteAllCategories() {
        if (bookRepository.findFirstBy().isPresent()) {
            throw new OperationNotAllowedException("You cant delete categories. Categories contains books");
        }
        bookCategoryRepository.deleteAll();
    }

    public BookCategoryDTO updateCategory(Long id, BookCategoryRequest bookCategoryRequest) {
        BookCategory categoryToUpdate = bookCategoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Category not found"));

        categoryToUpdate.setName(bookCategoryRequest.getName());

        return mapperEntity.mapBookCategoryToBookCategoryDTO(bookCategoryRepository.save(categoryToUpdate));
    }
}
