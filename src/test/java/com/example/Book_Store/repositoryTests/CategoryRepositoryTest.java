package com.example.Book_Store.repositoryTests;

import com.example.Book_Store.entities.Category;
import com.example.Book_Store.repository.BookRepository;
import com.example.Book_Store.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


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
    void shouldSaveCategory_Test() {
        //Given
        Category category = new Category(1, "test1");

        //When
        categoryRepository.save(category);

        //Then
        List<Category> categories = categoryRepository.findAll();

        assertFalse(categories.isEmpty());
        assertEquals(1, categories.size());
    }

    @Test
    void shouldFindCategoryById_Test() {
        //Given
        Category category = new Category(null, "test1");

        //When
        categoryRepository.save(category);

        //Then
        Optional<Category> foundCategory = categoryRepository.findById(category.getId());

        assertTrue(foundCategory.isPresent());
    }

    @Test
    void shouldFindCategoryByName_Test() {
        //Given
        Category category = new Category(null, "test1");

        //When
        categoryRepository.save(category);

        //Then
        Category foundCategory = categoryRepository.findCategoryByName(category.getName());

        assertNotNull(foundCategory);
        assertEquals(category.getId(), foundCategory.getId());
        assertEquals(category.getName(), foundCategory.getName());

    }

    @Test
    void shouldFindAllCategories_Test() {
        //Given
        Category category = new Category(null, "test1");
        Category category1 = new Category(null, "test2");

        //When
        categoryRepository.save(category);
        categoryRepository.save(category1);

        //Then
        List<Category> categories = categoryRepository.findAll();
        System.out.println("Categories: " + categories);

        assertEquals(2, categories.size());
        assertTrue(categories.contains(category));
        assertTrue(categories.contains(category1));
    }

    @Test
    void shouldDeleteCategoryById_Test() {
        //Given
        Category category = new Category(null, "test1");

        //When
        categoryRepository.save(category);

        //Then
        categoryRepository.deleteById(category.getId());

        List<Category> categories = categoryRepository.findAll();

        assertTrue(categories.isEmpty());

    }

    @Test
    void shouldDeleteAllCategories_Test() {
        //Given
        Category category = new Category(null, "test1");
        Category category1 = new Category(null, "test2");

        //When
        categoryRepository.save(category);
        categoryRepository.save(category1);

        //Then
        categoryRepository.deleteAll();

        List<Category> categories = categoryRepository.findAll();

        assertTrue(categories.isEmpty());

    }
}
