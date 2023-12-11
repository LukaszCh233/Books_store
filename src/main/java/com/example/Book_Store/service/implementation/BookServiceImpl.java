package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.Book;
import com.example.Book_Store.repository.BookRepository;
import com.example.Book_Store.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
   private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book findBookById(Integer id) {
        return bookRepository.findBookById(id);
    }

    @Override
    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public List<Book> findByCategoryName(String name) {
        return bookRepository.findByCategoryName(name);
    }

    @Override
    public List<Book> findByCategoryId(Integer id) {
        return bookRepository.findByCategoryId(id);
    }

    @Override
    public List<Book> findByPartialTitle(String partialTitle) {
        return bookRepository.findByPartialTitle(partialTitle);
    }

    @Override
    public void deleteBookById(Integer id) {
    bookRepository.deleteBookById(id);
    }

    @Override
    public void deleteBookByTitle(String title) {
bookRepository.deleteBookByTitle(title);
    }

    @Override
    public void deleteAllBooks() {
        bookRepository.deleteAll();
    }

    @Override
    public boolean existsBookById(Integer id) {
        return bookRepository.existsById(id);
    }

    @Override
    public boolean existsBookByTitle(String title) {
        return bookRepository.existsByTitle(title);
    }

    @Override
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }
}
