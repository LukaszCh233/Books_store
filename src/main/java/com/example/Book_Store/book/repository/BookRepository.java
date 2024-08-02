package com.example.Book_Store.book.repository;

import com.example.Book_Store.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = "SELECT * FROM books WHERE title LIKE %:bookTitle%", nativeQuery = true)
    List<Book> findBooksByBookTitle(@Param("bookTitle") String bookTitle);

    List<Book> findByCategoryName(String name);

    List<Book> findByCategoryId(Long id);
}

