package com.example.Book_Store.serviceTests;

import com.example.Book_Store.entities.Book;
import com.example.Book_Store.entities.Category;
import com.example.Book_Store.entities.Status;
import com.example.Book_Store.repository.BookRepository;
import com.example.Book_Store.repository.CategoryRepository;
import com.example.Book_Store.service.implementation.BookServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookServiceTest {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final BookServiceImpl bookService;

    @Autowired
    public BookServiceTest(CategoryRepository categoryRepository, BookRepository bookRepository, BookServiceImpl bookService) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
        this.bookService = bookService;
    }

    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void shouldFindBookById_ExistingBook_Test() {
        // Given
        Category category = new Category(null, "testCategory");

        Book book = new Book(null, "TestBook", "TestAuthor", 10.0, 10, Status.AVAILABLE, category);

        categoryRepository.save(category);
        bookRepository.save(book);

        // When
        Book foundBook = bookService.findBookById(book.getId());

        // Then
        assertEquals(book.getId(), foundBook.getId());
        assertEquals(book.getTitle(), foundBook.getTitle());
        assertEquals(book.getAuthor(), foundBook.getAuthor());
        assertEquals(book.getQuantity(), foundBook.getQuantity());
    }

    @Test
    void shouldFindBookById_NotExistingBook_Test() {
        int notExistingBookId = 1;

        assertThrows(EntityNotFoundException.class, () -> bookService.findBookById(notExistingBookId));
    }

    @Test
    void shouldFindBookByName_ExistingBook_Test() {
        //Given
        Category category = new Category(null, "testCategory");
        String bookName = "TestBook";
        Book book = new Book(null, bookName, "TestAuthor", 10.0, 10, Status.AVAILABLE, category);

        categoryRepository.save(category);
        bookRepository.save(book);

        //When
        List<Book> foundBooks = bookService.findByTitle(bookName);

        //Then
        assertEquals(1, foundBooks.size());
        assertEquals(book.getTitle(), foundBooks.get(0).getTitle());
        assertEquals(book.getAuthor(), foundBooks.get(0).getAuthor());
        assertEquals(book.getPrice(), foundBooks.get(0).getPrice());
    }

    @Test
    void shouldFindBookByName_NotExistingBook_Test() {
        String notExistingBookName = "TestBook";

        assertThrows(EntityNotFoundException.class, () -> bookService.findByTitle(notExistingBookName));
    }

    @Test
    void shouldFindBookByCategoryName_ExistingBook_Test() {
        //Given
        Category category = new Category(null, "testCategory");
        List<Book> books = Arrays.asList(
                new Book(null, "Book1", "Author1", 10.0, 10, Status.AVAILABLE, category),
                new Book(null, "Book2", "Author2", 15.0, 15, Status.AVAILABLE, category)
        );

        categoryRepository.save(category);
        bookRepository.saveAll(books);

        //When
        List<Book> foundBooks = bookService.findByCategoryName(category.getName());

        //Then
        assertEquals(2, foundBooks.size());
        assertEquals(books.get(0).getId(), foundBooks.get(0).getId());
        assertEquals(books.get(0).getTitle(), foundBooks.get(0).getTitle());
        assertEquals(books.get(0).getAuthor(), foundBooks.get(0).getAuthor());
        assertEquals(books.get(1).getId(), foundBooks.get(1).getId());
        assertEquals(books.get(1).getTitle(), foundBooks.get(1).getTitle());
        assertEquals(books.get(1).getAuthor(), foundBooks.get(1).getAuthor());
    }


    @Test
    void shouldFindBookByCategoryName_NotExistingBook_Test() {
        Category category = new Category(null, "testCategory");

        assertThrows(EntityNotFoundException.class, () -> bookService.findByCategoryName(category.getName()));
    }

    @Test
    void shouldFindBookByCategoryId_ExistingBook_Test() {
        //Given
        Category category = new Category(null, "testCategory");
        List<Book> books = Arrays.asList(
                new Book(null, "Book1", "Author1", 10.0, 10, Status.AVAILABLE, category),
                new Book(null, "Book2", "Author2", 15.0, 15, Status.AVAILABLE, category)
        );

        categoryRepository.save(category);
        bookRepository.saveAll(books);

        //When
        List<Book> foundBooks = bookService.findByCategoryId(category.getId());

        //Then
        assertEquals(2, foundBooks.size());
        assertEquals(books.get(0).getId(), foundBooks.get(0).getId());
        assertEquals(books.get(0).getTitle(), foundBooks.get(0).getTitle());
        assertEquals(books.get(0).getAuthor(), foundBooks.get(0).getAuthor());
        assertEquals(books.get(1).getId(), foundBooks.get(1).getId());
        assertEquals(books.get(1).getTitle(), foundBooks.get(1).getTitle());
        assertEquals(books.get(1).getAuthor(), foundBooks.get(1).getAuthor());
    }

    @Test
    void shouldFindBookByCategoryId_NotExistingBook_Test() {
        Category category = new Category(1, "testCategory");

        assertThrows(EntityNotFoundException.class, () -> bookService.findByCategoryId(category.getId()));

    }

    @Test
    void shouldFindAllBooks_EmptyList_Test() {

        assertThrows(EntityNotFoundException.class, bookService::findAllBooks);
    }

    @Test
    void shouldFindAllBooks_NonEmptyList_Test() {
        //Given
        Category category = new Category(null, "testCategory");

        List<Book> books = Arrays.asList(
                new Book(null, "Book1", "Author1", 10.0, 10, Status.AVAILABLE, category),
                new Book(null, "Book2", "Author2", 15.0, 15, Status.AVAILABLE, category));

        categoryRepository.save(category);
        bookRepository.saveAll(books);

        //When
        List<Book> foundBooks = bookService.findAllBooks();

        //Then
        assertEquals(2, foundBooks.size());
        assertEquals(books.get(0).getId(), foundBooks.get(0).getId());
        assertEquals(books.get(0).getTitle(), foundBooks.get(0).getTitle());
        assertEquals(books.get(0).getAuthor(), foundBooks.get(0).getAuthor());
        assertEquals(books.get(1).getId(), foundBooks.get(1).getId());
        assertEquals(books.get(1).getTitle(), foundBooks.get(1).getTitle());
        assertEquals(books.get(1).getAuthor(), foundBooks.get(1).getAuthor());
    }

    @Test
    void shouldDeleteAllBooks_EmptyList_Test() {

        assertThrows(EntityNotFoundException.class, bookService::deleteAllBooks);
    }

    @Test
    void deleteAllBooks_NotEmptyList_Test() {
        ///Given
        Category category = new Category(null, "testCategory");
        List<Book> books = Arrays.asList(
                new Book(null, "Book1", "Author1", 10.0, 10, Status.AVAILABLE, category),
                new Book(null, "Book2", "Author2", 15.0, 15, Status.AVAILABLE, category)
        );

        categoryRepository.save(category);
        bookRepository.saveAll(books);

        //When
        bookService.deleteAllBooks();

        //Then
        List<Book> foundBooks = bookRepository.findAll();

        assertTrue(foundBooks.isEmpty());
    }

    @Test
    void deleteBookById_ExistingBook_Test() {
        //Given
        Category category = new Category(null, "testCategory");
        Book book = new Book(null, "TestBook", "TestAuthor", 10.0, 10, Status.AVAILABLE, category);

        categoryRepository.save(category);
        bookRepository.save(book);

        //When
        bookService.deleteBookById(book.getId());
        Optional<Book> foundBook = bookRepository.findById(book.getId());
        //Then
        assertTrue(foundBook.isEmpty());
    }

    @Test
    void deleteBookById_NotExistingBook_Test() {
        int nonExistingBookId = 2;

        assertThrows(EntityNotFoundException.class, () -> bookService.deleteBookById(nonExistingBookId));

    }

    @Transactional
    @Test
    void deleteBookByTitle_ExistingBook_Test() {
        //Given
        Category category = new Category(null, "testCategory");

        String bookTitle = "TestBook";
        Book book = new Book(null, bookTitle, "TestAuthor", 10.0, 10, Status.AVAILABLE, category);

        categoryRepository.save(category);
        bookRepository.save(book);

        //When
        bookService.deleteBookByTitle(bookTitle);

        //Then
        List<Book> foundBooks = bookRepository.findAll();

        assertTrue(foundBooks.isEmpty());
    }

    @Test
    void deleteBookByTitle_NotExistingBook_Test() {
        String notExistingBookTitle = "NonExistingBook";

        assertThrows(EntityNotFoundException.class, () -> bookService.deleteBookByTitle(notExistingBookTitle));
    }

    @Test
    void updateBook_ExistingBook_Test() {
        //Given
        Category category = new Category(null, "testCategory");
        Book existingBook = new Book(null, "OldTitle", "OldAuthor", 10.0, 5, Status.AVAILABLE, category);
        Book updatedBook = new Book(null, "NewTitle", "NewAuthor", 15.0, 8, Status.AVAILABLE, category);

        categoryRepository.save(category);
        bookRepository.save(existingBook);

        //When
        Book resultBook = bookService.updateBook(existingBook.getId(), updatedBook);

        //Then
        assertEquals(existingBook.getId(), resultBook.getId());
        assertEquals("NewTitle", resultBook.getTitle());
        assertEquals("NewAuthor", resultBook.getAuthor());
        assertEquals(15.0, resultBook.getPrice());
        assertEquals(8, resultBook.getQuantity());
        assertEquals(Status.AVAILABLE, resultBook.getStatus());
        assertEquals(existingBook.getCategory().getId(), resultBook.getCategory().getId());
        assertEquals("testCategory", resultBook.getCategory().getName());
    }

    @Test
    void createBook_Test() {
        //Given
        Category category = new Category(null, "TestCategory");

        Book savedBook = new Book(null, "NewTitle", "NewAuthor", 15.0, 8, Status.AVAILABLE, category);

        categoryRepository.save(category);

        //When
        Book resultBook = bookService.createBook(savedBook);

        //Then
        assertEquals(savedBook.getId(), resultBook.getId());
        assertEquals("NewTitle", resultBook.getTitle());
        assertEquals("NewAuthor", resultBook.getAuthor());
        assertEquals(15.0, resultBook.getPrice());
        assertEquals(8, resultBook.getQuantity());
        assertEquals(Status.AVAILABLE, resultBook.getStatus());
        assertEquals(category, resultBook.getCategory());
    }
}
