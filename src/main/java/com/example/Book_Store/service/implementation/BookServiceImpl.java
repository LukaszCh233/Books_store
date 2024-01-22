package com.example.Book_Store.service.implementation;

import com.example.Book_Store.entities.Book;
import com.example.Book_Store.repository.BookRepository;
import com.example.Book_Store.service.BookService;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book findBookById(Integer id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Override
    public Book findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public List<Book> findByCategoryName(String name) {
        List<Book> books = bookRepository.findByCategoryName(name);
        if (!books.isEmpty()) {
            throw new ResourceNotFoundException("book list is empty");
        }
        return books;
    }

    @Override
    public List<Book> findByCategoryId(Integer id) {
        List<Book> books = bookRepository.findByCategoryId(id);
        if (!books.isEmpty()) {
            throw new ResourceNotFoundException("book list is empty");
        }
        return books;
    }

    @Override
    public void deleteBookById(Integer id) {

        Book bookToDelete = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        bookRepository.delete(bookToDelete);
    }

    @Override
    public void deleteBookByTitle(String title) {
        if (!existsBookByTitle(title)) {
            throw new ResourceNotFoundException("book not found");
        }
        bookRepository.deleteBookByTitle(title);
    }

    @Override
    public void deleteAllBooks() {
        List<Book> books = findAllBooks();
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("Books list is empty");
        }
        bookRepository.deleteAll();
    }

    @Override
    public boolean existsBookById(Integer id) {
        return bookRepository.existsById(id);
    }

    @Override
    public boolean existsBookByTitle(String title) {
        return bookRepository.existsByTitle(title);
    }

    @Override
    public Book createBook(Book book) {

        return bookRepository.save(book);
    }

    @Override
    public List<Book> findAllBooks() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("Book list is empty");
        }
        return bookRepository.findAll();
    }

    @Override
    public Book updateBook(Integer id, Book book) {
        Book existingBook = Optional.ofNullable(findBookById(id)).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        existingBook.setCategory(book.getCategory());
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setPrice(book.getPrice());
        existingBook.setQuantity(book.getQuantity());
        return bookRepository.save(existingBook);
    }
}
