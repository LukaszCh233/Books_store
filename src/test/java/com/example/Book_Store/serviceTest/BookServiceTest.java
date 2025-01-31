package com.example.Book_Store.serviceTest;

import com.example.Book_Store.store.book.dto.BookDTO;
import com.example.Book_Store.store.book.entity.Book;
import com.example.Book_Store.store.book.input.BookRequest;
import com.example.Book_Store.store.bookCategory.entity.BookCategory;
import com.example.Book_Store.store.book.repository.BookRepository;
import com.example.Book_Store.store.bookCategory.input.BookCategoryRequest;
import com.example.Book_Store.store.bookCategory.repository.BookCategoryRepository;
import com.example.Book_Store.store.book.service.BookService;
import com.example.Book_Store.store.bookCategory.service.BookCategoryService;
import com.example.Book_Store.store.Status;
import com.example.Book_Store.exceptions.OperationNotAllowedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@ActiveProfiles("test")
public class BookServiceTest {
    @Autowired
    BookService bookService;
    @Autowired
    BookCategoryService bookCategoryService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    BookCategoryRepository bookCategoryRepository;

    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    public void whenFindBookByTitleCanFindSimilarBooksTitle() {
        newBook("testBook");
        newBook("otherBook");

        List<BookDTO> bookList = bookService.findByTitle("Book");

        Assertions.assertEquals(bookList.size(), 2);
        Assertions.assertEquals(bookList.get(0).title(), "testBook");
        Assertions.assertEquals(bookList.get(1).title(), "otherBook");
    }

    @Test
    public void shouldFindBookByCategoryName() {
        BookCategory bookCategory = new BookCategory(null, "bookCategory");
        bookCategoryRepository.save(bookCategory);
        newBookWithCategory("book", bookCategory);

        BookCategoryRequest bookCategoryRequest = new BookCategoryRequest("bookCategory");

        List<BookDTO> bookList = bookService.findByCategoryName(bookCategoryRequest);

        Assertions.assertEquals(bookList.size(), 1);
        Assertions.assertEquals(bookList.get(0).title(), "book");
    }

    @Test
    public void createdBookShouldBeDisplayInBooksList() {
        BookCategory bookCategory = new BookCategory(null, "bookCategory");
        bookCategoryRepository.save(bookCategory);

        BookRequest book = new BookRequest("title", "author", 10.0, 100L,  bookCategory.getId());

        bookService.createBook(book);

        List<BookDTO> bookList = bookService.findAllBooks();

        Assertions.assertEquals(bookList.size(), 1);
        Assertions.assertEquals(bookList.get(0).title(), "title");
    }

    @Test
    public void whenUpdateBookQuantityIsZeroStatusShouldBeLack() {
        Book book = newBook("title");

        BookRequest updateBook = new BookRequest();
        updateBook.setTitle("newTitle");
        updateBook.setAuthor("author");
        updateBook.setPrice(10.0);
        updateBook.setQuantity(0L);
        updateBook.setCategoryId(book.getBookCategory().getId());


        bookService.updateBook(book.getId(), updateBook);

        Optional<Book> foundBook = bookRepository.findById(book.getId());

        Assertions.assertTrue(foundBook.isPresent());
        Assertions.assertEquals(0L, foundBook.get().getQuantity());
        Assertions.assertEquals(Status.LACK,foundBook.get().getStatus());
    }
    @Test
    public void deleteCategoryByIdShouldThrowExceptionWhenCategoryContainsBooks() {
        BookCategory bookCategory = new BookCategory(null, "bookCategory");
        bookCategoryRepository.save(bookCategory);

        newBookWithCategory("titleBook", bookCategory);

       Assertions.assertThrows(OperationNotAllowedException.class, () -> bookCategoryService.deleteCategoryById(bookCategory.getId()));
    }

    private Book newBook(String title) {
        BookCategory bookCategory = new BookCategory();
        bookCategory.setName("bookCategory");

        bookCategoryRepository.save(bookCategory);

        Book book = new Book();
        book.setBookCategory(bookCategory);
        book.setTitle(title);
        book.setAuthor("author");
        book.setPrice(10.0);
        book.setQuantity(100L);

        return bookRepository.save(book);
    }

    private Book newBookWithCategory(String title, BookCategory bookCategory) {

        Book book = new Book();
        book.setBookCategory(bookCategory);
        book.setTitle(title);
        book.setAuthor("author");
        book.setPrice(10.0);
        book.setQuantity(100L);
        return bookRepository.save(book);
    }
}
