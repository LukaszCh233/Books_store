package com.example.Book_Store.repositoryTest;

import com.example.Book_Store.store.bookCategory.entity.BookCategory;
import com.example.Book_Store.store.book.repository.BookRepository;
import com.example.Book_Store.store.bookCategory.repository.BookCategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class BookBookCategoryRepositoryTest {
    @Autowired
    BookCategoryRepository bookCategoryRepository;
    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
        bookCategoryRepository.deleteAll();
    }

    @Test
    public void findCategoryByNameIgnoreCase_test() {
        newCategory("test");
        Optional<BookCategory> foundCategory = bookCategoryRepository.findByNameIgnoreCase("TEST");

        Assertions.assertTrue(foundCategory.isPresent());
        assertEquals(foundCategory.get().getName(), "test");
    }

    private void newCategory(String name) {
        BookCategory bookCategory = new BookCategory(null, name);
        bookCategoryRepository.save(bookCategory);
    }
}
