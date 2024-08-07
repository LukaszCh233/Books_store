package com.example.Book_Store.book.service;

import com.example.Book_Store.book.entity.Book;
import com.example.Book_Store.book.repository.BookRepository;
import com.example.Book_Store.book.repository.CategoryRepository;
import com.example.Book_Store.enums.Status;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public BookService(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    public Book findBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
    }

    public List<Book> findByTitle(String title) {
        List<Book> books = bookRepository.findBooksByBookTitle(title);
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Books not found");
        }
        return books;
    }

    public List<Book> findByCategoryName(String name) {
        List<Book> books = bookRepository.findByCategoryName(name);
        if (books.isEmpty()) {
            throw new EntityNotFoundException("books not found");
        }
        return books;
    }

    public List<Book> findByCategoryId(Long id) {
        List<Book> books = bookRepository.findByCategoryId(id);
        if (books.isEmpty()) {
            throw new EntityNotFoundException("book list is empty");
        }
        return books;
    }

    public void deleteBookById(Long id) {
        Book bookToDelete = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

        bookRepository.delete(bookToDelete);
    }

    public void deleteBookByTitle(String title) {
        List<Book> bookToDelete = findByTitle(title);

        bookRepository.deleteAll(bookToDelete);
    }

    public void deleteAllBooks() {
        findAllBooks();
        bookRepository.deleteAll();
    }

    public Book createBook(Book book) {
        categoryRepository.findById(book.getCategory().getId())
                .orElseThrow(() -> new EntityNotFoundException("Invalid category ID"));

        return bookRepository.save(book);
    }

    public List<Book> findAllBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Book list is empty");
        }
        return books;
    }

    public Book updateBook(Long id, Book book) {
        Book existingBook = findBookById(id);

        existingBook.setCategory(book.getCategory());
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());
        existingBook.setQuantity(book.getQuantity());

        if (existingBook.getQuantity() == 0) {
            existingBook.setStatus(Status.LACK);
        }
        return bookRepository.save(existingBook);
    }
}
