package com.example.Book_Store.repository;

import com.example.Book_Store.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findBookById(Integer id);

    Book findByTitle(String title);

    List<Book> findByCategoryName(String name);

    List<Book> findByCategoryId(Integer id);

    @Modifying
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:partialTitle%")
    List<Book> findByPartialTitle(@Param("partialTitle") String partialTitle);

    void deleteBookById(Integer id);

    void deleteBookByTitle(String title);

    boolean existsById(Integer id);
    boolean existsByTitle(String title);
}

