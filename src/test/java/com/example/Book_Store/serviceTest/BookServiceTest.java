package com.example.Book_Store.serviceTest;

import com.example.Book_Store.book.entity.Book;
import com.example.Book_Store.book.entity.Category;
import com.example.Book_Store.book.repository.BookRepository;
import com.example.Book_Store.book.repository.CategoryRepository;
import com.example.Book_Store.book.service.BookService;
import com.example.Book_Store.book.service.CategoryService;
import com.example.Book_Store.enums.Status;
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
    CategoryService categoryService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    public void whenFindBookByTitleCanFindSimilarBooksTitle() {
        newBook("testBook");
        newBook("otherBook");

        List<Book> bookList = bookService.findByTitle("Book");

        Assertions.assertEquals(bookList.size(), 2);
        Assertions.assertEquals(bookList.get(0).getTitle(), "testBook");
        Assertions.assertEquals(bookList.get(1).getTitle(), "otherBook");
    }

    @Test
    public void shouldFindBookByCategoryName() {
        Category category = new Category(null, "category");
        categoryRepository.save(category);
        newBookWithCategory("book", category);

        List<Book> bookList = bookService.findByCategoryName("category");

        Assertions.assertEquals(bookList.size(), 1);
        Assertions.assertEquals(bookList.get(0).getTitle(), "book");
    }

    @Test
    public void createdBookShouldBeDisplayInBooksList() {
        Category category = new Category(null, "category");
        categoryRepository.save(category);

        Book book = new Book(null, "title", "author", 10.0, 100L, Status.AVAILABLE, category);

        bookService.createBook(book);

        List<Book> bookList = bookService.findAllBooks();

        Assertions.assertEquals(bookList.size(), 1);
        Assertions.assertEquals(bookList.get(0).getTitle(), "title");
    }

    @Test
    public void whenUpdateBookQuantityIsZeroStatusShouldBeLack() {
        Book book = newBook("title");

        Book updateBook = new Book();
        updateBook.setTitle("newTitle");
        updateBook.setAuthor("author");
        updateBook.setPrice(10.0);
        updateBook.setQuantity(0L);

        bookService.updateBook(book.getId(), updateBook);

        Optional<Book> foundBook = bookRepository.findById(book.getId());

        Assertions.assertTrue(foundBook.isPresent());
        Assertions.assertEquals(0L, foundBook.get().getQuantity());
        Assertions.assertEquals(Status.LACK,foundBook.get().getStatus());
    }
    @Test
    public void deleteCategoryByIdShouldThrowExceptionWhenCategoryContainsBooks() {
        Category category = new Category(null, "category");
        categoryRepository.save(category);

        newBookWithCategory("titleBook",category);

       Assertions.assertThrows(OperationNotAllowedException.class, () -> categoryService.deleteCategoryById(category.getId()));
    }

    private Book newBook(String title) {
        Category category = new Category(null, "category");
        categoryRepository.save(category);

        Book book = new Book();
        book.setCategory(category);
        book.setTitle(title);
        book.setAuthor("author");
        book.setPrice(10.0);
        book.setQuantity(100L);


        return bookRepository.save(book);
    }

    private Book newBookWithCategory(String title, Category category) {

        Book book = new Book();
        book.setCategory(category);
        book.setTitle(title);
        book.setAuthor("author");
        book.setPrice(10.0);
        book.setQuantity(100L);
        return bookRepository.save(book);
    }
}
