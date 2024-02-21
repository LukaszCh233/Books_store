package com.example.Book_Store.serviceTests;

import com.example.Book_Store.entities.Category;
import com.example.Book_Store.repository.BookRepository;
import com.example.Book_Store.repository.CategoryRepository;
import com.example.Book_Store.service.implementation.CategoryServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;


    @Test
    void shouldFindCategoryById_ExistingCategory_Test() {
        int categoryId = 1;
        Category mockCategory = new Category(1, "TestCategory");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mockCategory));

        Category resultCategory = categoryService.findCategoryById(categoryId);

        assertNotNull(resultCategory);
        assertEquals(categoryId, resultCategory.getId());
        assertEquals("TestCategory", resultCategory.getName());
    }

    @Test
    void shouldFindCategoryById_NotExistingCategory_Test() {
        int categoryId = 1;

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> categoryService.findCategoryById(categoryId));

        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    void shouldFindCategoryByName_ExistingCategory_Test() {
        String categoryName = "testName";
        Category mockCategory = new Category(1, categoryName);

        when(categoryRepository.findCategoryByName(categoryName)).thenReturn(mockCategory);

        Category resultCategory = categoryService.findCategoryByName(categoryName);

        assertNotNull(resultCategory);
        assertEquals(1, resultCategory.getId());
        assertEquals(categoryName, resultCategory.getName());
    }

    @Test
    void shouldFindCategoryByName_NotExistingCategory_Test() {
        String categoryName = "testName";

        when(categoryRepository.findCategoryByName(categoryName)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> categoryService.findCategoryByName(categoryName));

        verify(categoryRepository, times(1)).findCategoryByName(categoryName);
    }

    @Test
    void shouldDeleteCategoryById_CategoryExistsAndNoBooks_Test() {
        int categoryId = 1;
        Category category = new Category(categoryId, "testName");

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(bookRepository.findByCategoryId(categoryId)).thenReturn(Collections.emptyList());

        System.out.println("Before deletion: " + categoryService.categoryExistsById(categoryId));
        categoryService.deleteCategoryById(categoryId);
        System.out.println("After deletion: " + categoryService.categoryExistsById(categoryId));

        assertFalse(categoryService.categoryExistsById(categoryId));

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(bookRepository, times(1)).findByCategoryId(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    void shouldDeleteAllCategories_NotEmptyListNoBooks_Test() {
        Category category1 = new Category(1, "Category1");
        Category category2 = new Category(2, "Category2");
        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryRepository.findAll())
                .thenReturn(categories)
                .thenReturn(Collections.emptyList());

        when(bookRepository.findAll()).thenReturn(Collections.emptyList());


        categoryService.deleteAllCategories();

        verify(categoryRepository, times(1)).findAll();
        verify(bookRepository, times(1)).findAll();
        verify(categoryRepository, times(1)).deleteAll();
    }

    @Test
    void shouldFindAllCategories_EmptyList_Test() {

        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> categoryService.findAllCategories());

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void shouldFindAllCategories_NotEmptyList_Test() {

        Category category1 = new Category(1, "Category1");
        Category category2 = new Category(2, "Category2");
        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.findAllCategories();

        assertEquals(categories, result);

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void shouldUpdateCategory_Test() {

        int categoryId = 1;
        String updatedCategoryName = "UpdatedCategoryName";
        Category existingCategory = new Category(categoryId, "OldCategoryName");
        Category updatedCategory = new Category(categoryId, updatedCategoryName);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category resultCategory = categoryService.updateCategory(categoryId, updatedCategory);

        assertNotNull(resultCategory);
        assertEquals(categoryId, resultCategory.getId());
        assertEquals(updatedCategoryName, resultCategory.getName());

        verify(categoryRepository, times(1)).findById(categoryId);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void shouldCreateCategory_Test() {

        Category categoryToSave = new Category(1, "TestCategory");

        when(categoryRepository.save(categoryToSave)).thenReturn(categoryToSave);

        Category createdCategory = categoryService.createCategory(categoryToSave);

        verify(categoryRepository, times(1)).save(categoryToSave);

        assertNotNull(createdCategory);
        assertEquals(categoryToSave.getId(), createdCategory.getId());
        assertEquals(categoryToSave.getName(), createdCategory.getName());
    }
}

