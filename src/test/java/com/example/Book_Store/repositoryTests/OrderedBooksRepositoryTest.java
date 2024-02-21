package com.example.Book_Store.repositoryTests;

import com.example.Book_Store.entities.OrderedBooks;
import com.example.Book_Store.repository.OrderedBooksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
@ActiveProfiles("test")
public class OrderedBooksRepositoryTest {
    @Autowired
    OrderedBooksRepository orderedBooksRepository;

    @BeforeEach
    public void setUp() {
        orderedBooksRepository.deleteAll();
    }

    @Test
    void shouldSaveAll_Test() {
        //Given
        OrderedBooks orderedBooks = new OrderedBooks();
        OrderedBooks orderedBooks1 = new OrderedBooks();
        List<OrderedBooks> orderedBooksList = Arrays.asList(orderedBooks, orderedBooks1);

        //When
        orderedBooksRepository.saveAll(orderedBooksList);

        //Then
        List<OrderedBooks> savedOrderedBooksList = orderedBooksRepository.findAll();

        assertFalse(savedOrderedBooksList.isEmpty());
        assertEquals(2, savedOrderedBooksList.size());
    }
}
