package com.example.Book_Store.service;

import com.example.Book_Store.entities.Book;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookService {
   Book findBookById(Integer id);

    Book findByTitle(String title);

    List<Book> findByCategoryName(String name);

    List<Book> findByCategoryId(Integer id);

    @Modifying
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:partialTitle%")
    List<Book> findByPartialTitle(@Param("partialTitle") String partialTitle);

    void deleteBookById(Integer id);

    void deleteBookByTitle(String title);
    void deleteAllBooks();

    boolean existsBookById(Integer id);
 boolean existsBookByTitle(String title);
    Book createBook(Book book);
    List<Book> findAllBooks();
    Book updateBook(Book book);
}
