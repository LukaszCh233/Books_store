package com.example.Book_Store.serviceTest;

import com.example.Book_Store.book.entity.Book;
import com.example.Book_Store.book.repository.BookRepository;
import com.example.Book_Store.book.service.BookService;
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
public class BookServiceTest {
    @Autowired
    BookService bookService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    public void whenFindBookByTitleCanFindSimilarBooksTitle_test() {
        newBook("testBook");
        newBook("otherBook");

        List<Book> bookList = bookService.findByTitle("Book");

        Assertions.assertEquals(bookList.size(), 2);
        Assertions.assertEquals(bookList.get(0).getTitle(), "testBook");
        Assertions.assertEquals(bookList.get(1).getTitle(), "otherBook");
    }

    @Test
    public void shouldFindBookByCategoryName_test() {
        Category category = new Category(null, "category");
        categoryRepository.save(category);
        newBookWithCategory("book", category);

        List<Book> bookList = bookService.findByCategoryName("category");

        Assertions.assertEquals(bookList.size(), 1);
        Assertions.assertEquals(bookList.get(0).getTitle(), "book");
    }

    private void newBook(String title) {
        Category category = new Category(null, "category");
        categoryRepository.save(category);

        Book book = new Book();
        book.setCategory(category);
        book.setTitle(title);
        book.setAuthor("author");
        book.setPrice(10.0);
        book.setQuantity(100L);
        book.setStatus(Status.AVAILABLE);
        bookRepository.save(book);
    }

    private Book newBookWithCategory(String title, Category category) {

        Book book = new Book();
        book.setCategory(category);
        book.setTitle(title);
        book.setAuthor("author");
        book.setPrice(10.0);
        book.setQuantity(100L);
        book.setStatus(Status.AVAILABLE);
        return bookRepository.save(book);
    }
}
