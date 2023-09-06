package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.Book;
import com.example.Book_Store.repository.BookRepository;
import com.example.Book_Store.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class BookServiceImpl implements BookService {
    BookRepository bookRepository;
    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book findBookById(Long id) {
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
    public List<Book> findByCategoryId(Long id) {
        return bookRepository.findByCategoryId(id);
    }

    @Override
    public List<Book> findByPartialTitle(String partialTitle) {
        return bookRepository.findByPartialTitle(partialTitle);
    }

    @Override
    public void deleteBookById(Long id) {
    bookRepository.deleteBookById(id);
    }

    @Override
    public void deleteBookByTitle(String title) {
    bookRepository.deleteBookByTitle(title);
    }

    @Override
    public boolean existsBookById(Long id) {
        return bookRepository.existsBookById(id);
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
