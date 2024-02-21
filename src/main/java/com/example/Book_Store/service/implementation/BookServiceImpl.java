package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.Book;
import com.example.Book_Store.repository.BookRepository;
import com.example.Book_Store.service.BookService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book findBookById(Integer id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
    }

    @Override
    public List<Book> findByTitle(String title) {
        List<Book> books = bookRepository.findBooksByBookTitle(title);
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Books not found");
        }
        return books;
    }

    @Override
    public List<Book> findByCategoryName(String name) {
        List<Book> books = bookRepository.findByCategoryName(name);
        if (books.isEmpty()) {
            throw new EntityNotFoundException("books not found");
        }
        return books;
    }

    @Override
    public List<Book> findByCategoryId(Integer id) {
        List<Book> books = bookRepository.findByCategoryId(id);
        if (books.isEmpty()) {
            throw new EntityNotFoundException("book list is empty");
        }
        return books;
    }

    @Override
    public void deleteBookById(Integer id) {

        Book bookToDelete = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));
        bookRepository.delete(bookToDelete);
    }

    @Override
    public void deleteBookByTitle(String title) {
        findByTitle(title);
        bookRepository.deleteBookByTitle(title);
    }

    @Override
    public void deleteAllBooks() {

        bookRepository.deleteAll();
    }

    @Override
    public Book createBook(Book book) {

        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAllBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Book list is empty");
        }
        return books;
    }

    @Override
    public Book updateBook(Integer id, Book book) {
        Book existingBook = findBookById(id);

        existingBook.setCategory(book.getCategory());
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());
        existingBook.setQuantity(book.getQuantity());
        return bookRepository.save(existingBook);
    }
}
