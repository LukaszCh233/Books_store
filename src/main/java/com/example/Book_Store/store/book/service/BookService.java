package com.example.Book_Store.store.book.service;

import com.example.Book_Store.mapper.MapperEntity;
import com.example.Book_Store.store.Status;
import com.example.Book_Store.store.book.dto.BookDTO;
import com.example.Book_Store.store.book.entity.Book;
import com.example.Book_Store.store.book.input.BookRequest;
import com.example.Book_Store.store.book.repository.BookRepository;
import com.example.Book_Store.store.bookCategory.entity.BookCategory;
import com.example.Book_Store.store.bookCategory.input.BookCategoryRequest;
import com.example.Book_Store.store.bookCategory.repository.BookCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;
    private final MapperEntity mapperEntity;

    public BookService(BookRepository bookRepository, BookCategoryRepository bookCategoryRepository,
                       MapperEntity mapperEntity) {
        this.bookRepository = bookRepository;
        this.bookCategoryRepository = bookCategoryRepository;
        this.mapperEntity = mapperEntity;
    }

    public BookDTO findBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Book not found with id: " + id));

        return mapperEntity.mapBookToBookDTO(book);
    }

    public List<BookDTO> findByTitle(String title) {
        List<Book> books = bookRepository.findBooksByBookTitle(title);
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Books not found");
        }
        return mapperEntity.mapBookListToBookListDTO(books);
    }

    public List<BookDTO> findByCategoryName(BookCategoryRequest bookCategoryRequest) {
        List<Book> books = bookRepository.findByBookCategoryName(bookCategoryRequest.getName());
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Books not found");
        }
        return mapperEntity.mapBookListToBookListDTO(books);
    }

    public List<BookDTO> findByCategoryId(Long id) {
        List<Book> books = bookRepository.findByBookCategoryId(id);
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Book list is empty");
        }
        return mapperEntity.mapBookListToBookListDTO(books);
    }

    public void deleteBookById(Long id) {
        Book bookToDelete = bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Book not found with id: " + id));

        bookRepository.delete(bookToDelete);
    }

    public void deleteAllBooks() {
        bookRepository.deleteAll();
    }

    public BookDTO createBook(BookRequest bookRequest) {
        BookCategory category = bookCategoryRepository.findById(bookRequest.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Invalid category ID"));

        Book book = new Book();
        book.setTitle(bookRequest.getTitle());
        book.setAuthor(bookRequest.getAuthor());
        book.setPrice(bookRequest.getPrice());
        book.setQuantity(bookRequest.getQuantity());
        book.setBookCategory(category);

        return mapperEntity.mapBookToBookDTO(bookRepository.save(book));
    }

    public List<BookDTO> findAllBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            throw new EntityNotFoundException("Book list is empty");
        }
        return mapperEntity.mapBookListToBookListDTO(books);
    }

    public BookDTO updateBook(Long id, BookRequest bookRequest) {
        Book bookToUpdate = bookRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Book not found"));

        BookCategory bookCategory = bookCategoryRepository.findById(bookRequest.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        bookToUpdate.setBookCategory(bookCategory);
        bookToUpdate.setTitle(bookRequest.getTitle());
        bookToUpdate.setAuthor(bookRequest.getAuthor());
        bookToUpdate.setPrice(bookRequest.getPrice());
        bookToUpdate.setQuantity(bookRequest.getQuantity());

        if (bookToUpdate.getQuantity() == 0) {
            bookToUpdate.setStatus(Status.LACK);
        }
        return mapperEntity.mapBookToBookDTO(bookRepository.save(bookToUpdate));
    }
}
