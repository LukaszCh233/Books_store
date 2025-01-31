package com.example.Book_Store.repositoryTest;

import com.example.Book_Store.store.book.entity.Book;
import com.example.Book_Store.store.bookCategory.entity.BookCategory;
import com.example.Book_Store.store.book.repository.BookRepository;
import com.example.Book_Store.store.bookCategory.repository.BookCategoryRepository;
import com.example.Book_Store.store.Status;
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
    BookCategoryRepository bookCategoryRepository;

    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
        bookCategoryRepository.deleteAll();
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
        BookCategory bookCategory = newCategory("testCategory");
        newBookWithCategory("test", bookCategory);

        List<Book> bookList = bookRepository.findByBookCategoryId(bookCategory.getId());

        Assertions.assertEquals(bookList.size(), 1);
        Assertions.assertEquals(bookList.get(0).getTitle(), "test");
    }

    @Test
    public void findBooksByCategoryName_test() {
        BookCategory bookCategory = newCategory("testCategory");
        newBookWithCategory("test", bookCategory);

        List<Book> bookList = bookRepository.findByBookCategoryName("testCategory");

        Assertions.assertEquals(bookList.size(), 1);
        Assertions.assertEquals(bookList.get(0).getTitle(), "test");
    }

    private void newBook(String title) {
        BookCategory bookCategory = new BookCategory(null, "bookCategory");
        bookCategoryRepository.save(bookCategory);
        Book book = new Book();
        book.setBookCategory(bookCategory);
        book.setTitle(title);
        book.setAuthor("test");
        book.setPrice(100.0);
        book.setQuantity(1L);
        book.setStatus(Status.AVAILABLE);
        bookRepository.save(book);
    }

    private void newBookWithCategory(String title, BookCategory bookCategory) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor("test");
        book.setTitle(title);
        book.setAuthor("test");
        book.setBookCategory(bookCategory);
        book.setPrice(100.0);
        book.setQuantity(1L);
        book.setStatus(Status.AVAILABLE);
        bookRepository.save(book);
    }

    private BookCategory newCategory(String name) {
        BookCategory bookCategory = new BookCategory(null, name);
        return bookCategoryRepository.save(bookCategory);
    }
}
