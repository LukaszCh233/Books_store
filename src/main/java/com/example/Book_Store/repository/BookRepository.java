package com.example.Book_Store.repository;

import com.example.Book_Store.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Book findBookById(Long id);

    Book findByTitle(String title);

    List<Book> findByCategoryName(String name);

    List<Book> findByCategoryId(Long id);

    @Modifying
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:partialTitle%")
    List<Book> findByPartialTitle(@Param("partialTitle") String partialTitle);

    void deleteBookById(Long id);

    void deleteBookByTitle(String title);

    boolean existsBookById(Long id);
}

