package com.example.Book_Store.repository;

import com.example.Book_Store.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findCategoryById(Integer id);

    Optional<Category> findCategoryByName(String name);

    void deleteCategoryById(Integer id);
    boolean existsById(Integer id);
}
