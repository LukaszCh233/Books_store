package com.example.Book_Store.service;

import com.example.Book_Store.entities.Book;

import java.util.List;

public interface BookService {
    Book findBookById(Integer id);

    List<Book> findByTitle(String title);

    List<Book> findByCategoryName(String name);

    List<Book> findByCategoryId(Integer id);

    void deleteBookById(Integer id);

    void deleteBookByTitle(String title);

    void deleteAllBooks();

    Book createBook(Book book);

    List<Book> findAllBooks();

    Book updateBook(Integer id, Book book);
}
