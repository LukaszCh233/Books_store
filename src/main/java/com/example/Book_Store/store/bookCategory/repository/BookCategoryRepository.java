package com.example.Book_Store.store.bookCategory.repository;

import com.example.Book_Store.store.bookCategory.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
    @Query("SELECT c FROM BookCategory c WHERE LOWER(c.name) = LOWER(:name)")
    Optional<BookCategory> findByNameIgnoreCase(@Param("name") String name);
}
