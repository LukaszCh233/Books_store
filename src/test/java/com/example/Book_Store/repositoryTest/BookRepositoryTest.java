package com.example.Book_Store.repositoryTest;

import com.example.Book_Store.book.entity.Book;
import com.example.Book_Store.book.repository.BookRepository;
import com.example.Book_Store.book.entity.Category;
import com.example.Book_Store.book.repository.CategoryRepository;
import com.example.Book_Store.enums.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void findBookByTitle_test() {
        newBook("book");
        newBook("other");

        List<Book> bookList = bookRepository.findBooksByBookTitle("book");

        Assertions.assertEquals(bookList.size(), 1);
        Assertions.assertEquals(bookList.get(0).getTitle(), "book");
    }

    @Test
    public void findBooksByCategoryId_test() {
        Category category = newCategory("testCategory");
        newBookWithCategory("test", category);

        List<Book> bookList = bookRepository.findByCategoryId(category.getId());

        Assertions.assertEquals(bookList.size(), 1);
        Assertions.assertEquals(bookList.get(0).getTitle(), "test");
    }

    @Test
    public void findBooksByCategoryName_test() {
        Category category = newCategory("testCategory");
        newBookWithCategory("test", category);

        List<Book> bookList = bookRepository.findByCategoryName("testCategory");

        Assertions.assertEquals(bookList.size(), 1);
        Assertions.assertEquals(bookList.get(0).getTitle(), "test");
    }

    private void newBook(String title) {
        Category category = new Category(null, "category");
        categoryRepository.save(category);
        Book book = new Book();
        book.setCategory(category);
        book.setTitle(title);
        book.setAuthor("test");
        book.setPrice(100.0);
        book.setQuantity(1L);
        book.setStatus(Status.AVAILABLE);
        bookRepository.save(book);
    }

    private void newBookWithCategory(String title, Category category) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor("test");
        book.setTitle(title);
        book.setAuthor("test");
        book.setCategory(category);
        book.setPrice(100.0);
        book.setQuantity(1L);
        book.setStatus(Status.AVAILABLE);
        bookRepository.save(book);
    }

    private Category newCategory(String name) {
        Category category = new Category(null, name);
        return categoryRepository.save(category);
    }
}
