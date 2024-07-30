package com.example.Book_Store.repositoryTest;

import com.example.Book_Store.book.repository.BookRepository;
import com.example.Book_Store.book.entity.Category;
import com.example.Book_Store.book.repository.CategoryRepository;
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
public class CategoryRepositoryTest {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void findCategoryByNameIgnoreCase_test() {
        newCategory("test");
        Optional<Category> foundCategory = categoryRepository.findByNameIgnoreCase("TEST");

        Assertions.assertTrue(foundCategory.isPresent());
        assertEquals(foundCategory.get().getName(), "test");
    }

    private void newCategory(String name) {
        Category category = new Category(null, name);
        categoryRepository.save(category);
    }
}
