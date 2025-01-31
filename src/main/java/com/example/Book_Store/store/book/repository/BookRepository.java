package com.example.Book_Store.store.book.repository;

import com.example.Book_Store.store.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = "SELECT * FROM books WHERE title LIKE %:bookTitle%", nativeQuery = true)
    List<Book> findBooksByBookTitle(@Param("bookTitle") String bookTitle);

    List<Book> findByBookCategoryName(String name);

    List<Book> findByBookCategoryId(Long id);

    boolean existsByBookCategoryId(Long id);

    Optional<Book> findFirstBy();
}

