package com.example.Book_Store.repository;

import com.example.Book_Store.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query(value = "SELECT * FROM books WHERE title LIKE %:bookTitle%", nativeQuery = true)
    List<Book> findBooksByBookTitle(@Param("bookTitle") String bookTitle);

    List<Book> findByCategoryName(String name);

    List<Book> findByCategoryId(Integer id);

    void deleteBookByTitle(String title);

    boolean existsById(Integer id);

}

