package com.example.Book_Store.repositoryTests;

import com.example.Book_Store.entities.Book;
import com.example.Book_Store.entities.Category;
import com.example.Book_Store.entities.Status;
import com.example.Book_Store.repository.BookRepository;
import com.example.Book_Store.repository.CategoryRepository;
import jakarta.transaction.Transactional;
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
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    void shouldFindBooksByBookTitle_Test() {
        //Given
        Book book = new Book(1, "testTitle", null, 10, 5, Status.AVAILABLE, null);

        //When
        bookRepository.save(book);

        //Then
        List<Book> foundBooks = bookRepository.findBooksByBookTitle(book.getTitle());

        assertNotNull(foundBooks);
        assertEquals(1, foundBooks.size());
    }

    @Test
    void shouldFindBookByCategoryName_Test() {
        //Given
        Category category = new Category(null, "test");
        Book book = new Book(1, "testTitle", null, 10, 5, Status.AVAILABLE, category);

        //When
        categoryRepository.save(category);
        bookRepository.save(book);

        //Then
        List<Book> foundBooks = bookRepository.findByCategoryName(category.getName());

        assertNotNull(foundBooks);
        assertEquals(1, foundBooks.size());

    }

    @Test
    void shouldFindBooksByCategoryId_Test() {
        //Given
        Category category = new Category(null, "test");
        Book book = new Book(1, "testTitle", null, 10, 5, Status.AVAILABLE, category);

        //When
        categoryRepository.save(category);
        bookRepository.save(book);

        //Then
        List<Book> foundBooks = bookRepository.findByCategoryId(category.getId());

        assertNotNull(foundBooks);
        assertEquals(1, foundBooks.size());

    }

    @Test
    void shouldSaveBook_Test() {
        //Given
        Book book = new Book(null, "testTitle", null, 10, 5, Status.AVAILABLE, null);

        //When
        bookRepository.save(book);

        //Then
        Optional<Book> foundBookOptional = bookRepository.findById(book.getId());
        Book foundBook = foundBookOptional.get();

        assertEquals(foundBook.getId(), book.getId());
        assertEquals(book.getTitle(), foundBook.getTitle());

    }

    @Test
    void shouldFindBookById_Test() {
        //Given
        Book book = new Book(null, "testTitle", null, 10, 5, Status.AVAILABLE, null);

        //When
        bookRepository.save(book);

        //Then
        Optional<Book> foundBookOptional = bookRepository.findById(book.getId());

        assertTrue(foundBookOptional.isPresent());
    }

    @Transactional
    @Test
    void shouldDeleteBookByTitle_Test() {
        //Given
        Book book = new Book(null, "testTitle", null, 10, 5, Status.AVAILABLE, null);

        //When
        bookRepository.save(book);

        //Then
        bookRepository.deleteBookByTitle(book.getTitle());
        List<Book> foundBooks = bookRepository.findAll();

        assertTrue(foundBooks.isEmpty());

    }

    @Test
    void shouldDeleteAllBooks_Test() {
        //Given
        Book book = new Book(null, "testTitle", null, 10, 5, Status.AVAILABLE, null);
        Book book1 = new Book(null, "testTitle1", null, 10, 5, Status.AVAILABLE, null);

        //When
        bookRepository.save(book);
        bookRepository.save(book1);

        //Then
        bookRepository.deleteAll();
        List<Book> foundBooks = bookRepository.findAll();

        assertTrue(foundBooks.isEmpty());
    }
}
