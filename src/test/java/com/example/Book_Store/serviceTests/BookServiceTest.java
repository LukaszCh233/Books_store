package com.example.Book_Store.serviceTests;

import com.example.Book_Store.entities.Book;
import com.example.Book_Store.entities.Category;
import com.example.Book_Store.entities.Status;
import com.example.Book_Store.repository.BookRepository;
import com.example.Book_Store.service.implementation.BookServiceImpl;
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
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void shouldFindBookById_ExistingBook_Test() {
        Category category = new Category(1, "testCategory");
        int bookId = 1;
        Book mockBook = new Book(bookId, "TestBook", "TestAuthor", 10.0, 10, Status.AVAILABLE, category);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        Book resultBook = bookService.findBookById(bookId);

        verify(bookRepository, times(1)).findById(bookId);

        assertEquals(mockBook, resultBook);

    }

    @Test
    void shouldFindBookById_NotExistingBook_Test() {
        int notExistingBookId = 99;

        when(bookRepository.findById(notExistingBookId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.findBookById(notExistingBookId));

        verify(bookRepository, times(1)).findById(notExistingBookId);
    }

    @Test
    void shouldFindBookByName_ExistingBook_Test() {
        Category category = new Category(1, "testCategory");
        String bookName = "TestBook";
        Book mockBook = new Book(1, bookName, "TestAuthor", 10.0, 10, Status.AVAILABLE, category);

        when(bookRepository.findBooksByBookTitle(bookName)).thenReturn(Collections.singletonList(mockBook));

        List<Book> resultBook = bookService.findByTitle(bookName);

        verify(bookRepository, times(1)).findBooksByBookTitle(bookName);

        assertIterableEquals(Collections.singletonList(mockBook), resultBook);
    }

    @Test
    void shouldFindBookByName_NotExistingBook_Test() {
        String notExistingBookName = "TestBook";

        when(bookRepository.findBooksByBookTitle(notExistingBookName)).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> bookService.findByTitle(notExistingBookName));

        verify(bookRepository, times(1)).findBooksByBookTitle(notExistingBookName);
    }

    @Test
    void shouldFindBookByCategoryName_ExistingBook_Test() {
        Category category = new Category(1, "testCategory");
        List<Book> mockBooks = Arrays.asList(
                new Book(1, "Book1", "Author1", 10.0, 10, Status.AVAILABLE, category),
                new Book(2, "Book2", "Author2", 15.0, 15, Status.AVAILABLE, category)
        );
        when(bookRepository.findByCategoryName(category.getName())).thenReturn(mockBooks);

        List<Book> resultBooks = bookService.findByCategoryName(category.getName());

        verify(bookRepository, times(1)).findByCategoryName(category.getName());

        assertIterableEquals(mockBooks, resultBooks);
    }


    @Test
    void shouldFindBookByCategoryName_NotExistingBook_Test() {
        Category category = new Category(1, "testCategory");

        when(bookRepository.findByCategoryName(category.getName())).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> bookService.findByCategoryName(category.getName()));

        verify(bookRepository, times(1)).findByCategoryName(category.getName());
    }

    @Test
    void shouldFindBookByCategoryId_ExistingBook_Test() {
        Category category = new Category(1, "testCategory");
        List<Book> mockBooks = Arrays.asList(
                new Book(1, "Book1", "Author1", 10.0, 10, Status.AVAILABLE, category),
                new Book(2, "Book2", "Author2", 15.0, 15, Status.AVAILABLE, category)
        );
        when(bookRepository.findByCategoryId(category.getId())).thenReturn(mockBooks);

        List<Book> resultBooks = bookService.findByCategoryId(category.getId());

        verify(bookRepository, times(1)).findByCategoryId(category.getId());

        assertIterableEquals(mockBooks, resultBooks);
    }

    @Test
    void shouldFindBookByCategoryId_NotExistingBook_Test() {
        Category category = new Category(1, "testCategory");

        when(bookRepository.findByCategoryId(category.getId())).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> bookService.findByCategoryId(category.getId()));

        verify(bookRepository, times(1)).findByCategoryId(category.getId());
    }

    @Test
    void shouldFindAllBooks_EmptyList_Test() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> bookService.findAllBooks());

        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void shouldFindAllBooks_NonEmptyList_Test() {
        Category category = new Category(1, "testCategory");

        List<Book> mockBooks = Arrays.asList(
                new Book(1, "Book1", "Author1", 10.0, 10, Status.AVAILABLE, category),
                new Book(2, "Book2", "Author2", 15.0, 15, Status.AVAILABLE, category));

        when(bookRepository.findAll()).thenReturn(mockBooks);

        List<Book> resultBooks = bookService.findAllBooks();

        verify(bookRepository, times(1)).findAll();

        assertIterableEquals(mockBooks, resultBooks);
    }

    @Test
    void shouldDeleteAllBooks_EmptyList_Test() {

        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> bookService.deleteAllBooks());

        verify(bookRepository, times(1)).deleteAll();
    }

    @Test
    void deleteAllBooks_NotEmptyList_Test() {
        Category category = new Category(1, "testCategory");
        List<Book> mockBooks = Arrays.asList(
                new Book(1, "Book1", "Author1", 10.0, 10, Status.AVAILABLE, category),
                new Book(2, "Book2", "Author2", 15.0, 15, Status.AVAILABLE, category)
        );

        when(bookRepository.findAll()).thenReturn(mockBooks);

        assertDoesNotThrow(() -> bookService.deleteAllBooks());

        verify(bookRepository, times(1)).deleteAll();
    }

    @Test
    void deleteBookById_ExistingBook_Test() {
        int bookId = 1;
        Category category = new Category(1, "testCategory");
        Book mockBook = new Book(bookId, "TestBook", "TestAuthor", 10.0, 10, Status.AVAILABLE, category);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(mockBook));

        assertDoesNotThrow(() -> bookService.deleteBookById(bookId));

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).delete(mockBook);
    }

    @Test
    void deleteBookById_NotExistingBook_Test() {
        int nonExistingBookId = 2;

        when(bookRepository.findById(nonExistingBookId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> bookService.deleteBookById(nonExistingBookId));

        verify(bookRepository, times(1)).findById(nonExistingBookId);
        verify(bookRepository, times(0)).delete(any());
    }

    @Test
    void deleteBookByTitle_ExistingBook_Test() {
        String bookTitle = "TestBook";
        Book mockBook = new Book(1, bookTitle, "TestAuthor", 10.0, 10, Status.AVAILABLE, new Category(1, "TestCategory"));

        when(bookRepository.findBooksByBookTitle(bookTitle)).thenReturn(Collections.singletonList(mockBook));

        assertDoesNotThrow(() -> bookService.deleteBookByTitle(bookTitle));

        verify(bookRepository, times(1)).findBooksByBookTitle(bookTitle);
        verify(bookRepository, times(1)).deleteBookByTitle(bookTitle);
    }

    @Test
    void deleteBookByTitle_NotExistingBook_Test() {
        String notExistingBookTitle = "NonExistingBook";

        when(bookRepository.findBooksByBookTitle(notExistingBookTitle)).thenReturn(Collections.emptyList());

        assertThrows(EntityNotFoundException.class, () -> bookService.deleteBookByTitle(notExistingBookTitle));

        verify(bookRepository, times(1)).findBooksByBookTitle(notExistingBookTitle);
        verify(bookRepository, times(0)).deleteBookByTitle(notExistingBookTitle);
    }

    @Test
    void updateBook_ExistingBook_Test() {
        int bookId = 1;
        Category category = new Category(1, "testCategory");
        Book existingBook = new Book(bookId, "OldTitle", "OldAuthor", 10.0, 5, Status.AVAILABLE, category);
        Book updatedBook = new Book(bookId, "NewTitle", "NewAuthor", 15.0, 8, Status.AVAILABLE, category);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Book resultBook = bookService.updateBook(bookId, updatedBook);

        assertEquals(bookId, resultBook.getId());
        assertEquals("NewTitle", resultBook.getTitle());
        assertEquals("NewAuthor", resultBook.getAuthor());
        assertEquals(15.0, resultBook.getPrice());
        assertEquals(8, resultBook.getQuantity());
        assertEquals(Status.AVAILABLE, resultBook.getStatus());
        assertEquals(1, resultBook.getCategory().getId());
        assertEquals("testCategory", resultBook.getCategory().getName());

        verify(bookRepository, times(1)).findById(bookId);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void createBook_Test() {
        Category category = new Category(1, "TestCategory");

        Book savedBook = new Book(1, "NewTitle", "NewAuthor", 15.0, 8, Status.AVAILABLE, category);

        when(bookRepository.save(savedBook)).thenReturn(savedBook);

        Book resultBook = bookService.createBook(savedBook);

        assertEquals(savedBook.getId(), resultBook.getId());
        assertEquals("NewTitle", resultBook.getTitle());
        assertEquals("NewAuthor", resultBook.getAuthor());
        assertEquals(15.0, resultBook.getPrice());
        assertEquals(8, resultBook.getQuantity());
        assertEquals(Status.AVAILABLE, resultBook.getStatus());
        assertEquals(category, resultBook.getCategory());

        verify(bookRepository, times(1)).save(savedBook);
    }
}
