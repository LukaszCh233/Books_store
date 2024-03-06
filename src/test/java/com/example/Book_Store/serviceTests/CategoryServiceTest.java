package com.example.Book_Store.serviceTests;

import com.example.Book_Store.entities.Category;
import com.example.Book_Store.repository.BookRepository;
import com.example.Book_Store.repository.CategoryRepository;
import com.example.Book_Store.service.implementation.CategoryServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryServiceTest {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final CategoryServiceImpl categoryService;

    @Autowired
    public CategoryServiceTest(CategoryRepository categoryRepository, BookRepository bookRepository, CategoryServiceImpl categoryService) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
    }

    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void shouldFindCategoryById_ExistingCategory_Test() {
        //Given
        Category category = new Category(null, "TestCategory");

        categoryRepository.save(category);

        //When
        Category foundCategory = categoryService.findCategoryById(category.getId());

        //Then
        assertNotNull(foundCategory);
        assertEquals(category.getId(), foundCategory.getId());
        assertEquals(category.getName(), foundCategory.getName());
    }

    @Test
    void shouldFindCategoryById_NotExistingCategory_Test() {
        int categoryId = 1;

        assertThrows(EntityNotFoundException.class, () -> categoryService.findCategoryById(categoryId));
    }

    @Test
    void shouldFindCategoryByName_ExistingCategory_Test() {
        //Given
        String categoryName = "testName";
        Category category = new Category(null, categoryName);

        categoryRepository.save(category);

        //When
        Category foundCategory = categoryService.findCategoryByName(categoryName);

        //Then
        assertNotNull(foundCategory);
        assertEquals(category.getId(), foundCategory.getId());
        assertEquals(categoryName, foundCategory.getName());
    }

    @Test
    void shouldFindCategoryByName_NotExistingCategory_Test() {
        String categoryName = "testName";

        assertThrows(EntityNotFoundException.class, () -> categoryService.findCategoryByName(categoryName));
    }

    @Test
    void shouldDeleteCategoryById_CategoryExistsAndNoBooks_Test() {
        //Given
        Category category = new Category(null, "testName");

        categoryRepository.save(category);

        //When
        categoryService.deleteCategoryById(category.getId());

        //Then
        assertFalse(categoryService.categoryExistsById(category.getId()));
    }

    @Test
    void shouldDeleteAllCategories_NotEmptyListNoBooks_Test() {
        //Given
        Category category1 = new Category(1, "Category1");
        Category category2 = new Category(2, "Category2");
        List<Category> categories = Arrays.asList(category1, category2);

        categoryRepository.saveAll(categories);

        //When
        categoryService.deleteAllCategories();

        //Then
        List<Category> categoryList = categoryRepository.findAll();

        assertTrue(categoryList.isEmpty());
    }

    @Test
    void shouldFindAllCategories_EmptyList_Test() {

        assertThrows(EntityNotFoundException.class, categoryService::findAllCategories);
    }

    @Test
    void shouldFindAllCategories_NotEmptyList_Test() {
        //Given
        Category category1 = new Category(null, "Category1");
        Category category2 = new Category(null, "Category2");
        List<Category> categories = Arrays.asList(category1, category2);

        categoryRepository.saveAll(categories);

        //When
        List<Category> result = categoryService.findAllCategories();

        //Then
        assertEquals(categories.get(0).getId(), result.get(0).getId());
        assertEquals(categories.get(0).getName(), result.get(0).getName());
        assertEquals(categories.get(1).getId(), result.get(1).getId());
        assertEquals(categories.get(1).getName(), result.get(1).getName());
    }

    @Test
    void shouldUpdateCategory_Test() {
        //Given
        String updatedCategoryName = "UpdatedCategoryName";
        Category existingCategory = new Category(null, "OldCategoryName");
        Category updatedCategory = new Category(null, updatedCategoryName);

        categoryRepository.save(existingCategory);

        //When
        Category resultCategory = categoryService.updateCategory(existingCategory.getId(), updatedCategory);

        //Then
        assertNotNull(resultCategory);
        assertEquals(existingCategory.getId(), resultCategory.getId());
        assertEquals(updatedCategory.getName(), resultCategory.getName());
    }

    @Test
    void shouldCreateCategory_Test() {
        //Given
        Category category = new Category(null, "TestCategory");

        categoryRepository.save(category);

        //When
        Category createdCategory = categoryService.createCategory(category);

        //Then
        assertNotNull(createdCategory);
        assertEquals(category.getId(), createdCategory.getId());
        assertEquals(category.getName(), createdCategory.getName());
    }
}

