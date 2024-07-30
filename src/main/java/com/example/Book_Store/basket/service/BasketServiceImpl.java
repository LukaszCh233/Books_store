package com.example.Book_Store.basket.service;

import com.example.Book_Store.basket.dto.BasketDTO;
import com.example.Book_Store.basket.entity.Basket;
import com.example.Book_Store.basket.entity.BasketProducts;
import com.example.Book_Store.basket.repository.BasketProductsRepository;
import com.example.Book_Store.basket.repository.BasketRepository;
import com.example.Book_Store.book.entity.Book;
import com.example.Book_Store.book.repository.BookRepository;
import com.example.Book_Store.enums.Status;
import com.example.Book_Store.exceptions.NotEnoughBooksException;
import com.example.Book_Store.mapper.MapperEntity;
import com.example.Book_Store.user.entity.Customer;
import com.example.Book_Store.user.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class BasketServiceImpl {

    private final BasketRepository basketRepository;
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;
    private final BasketProductsRepository basketProductsRepository;
    private final MapperEntity mapperEntity;

    public BasketServiceImpl(BasketRepository basketRepository, CustomerRepository customerRepository,
                             BookRepository bookRepository, BasketProductsRepository basketProductsRepository,
                             MapperEntity mapperEntity) {
        this.basketRepository = basketRepository;
        this.customerRepository = customerRepository;
        this.bookRepository = bookRepository;
        this.basketProductsRepository = basketProductsRepository;
        this.mapperEntity = mapperEntity;
    }

    public void addBookToBasket(Long idBook, Long quantity, Principal principal) {
        String username = principal.getName();
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Book selectedBook = bookRepository.findById(idBook).orElseThrow(() -> new EntityNotFoundException("not found Book"));

        if (selectedBook.getStatus().equals(Status.LACK)) {
            throw new NotEnoughBooksException(" Book is unavailable" + idBook);
        }
        if (selectedBook.getQuantity() < quantity) {
            throw new NotEnoughBooksException("There are not enough copies of the book with ID: " + idBook);
        }
        Basket basket = Optional.ofNullable(basketRepository.findBasketByUserId(customer.getId()))
                .orElseGet(() -> {
                    Basket newBasket = new Basket();
                    newBasket.setUserId(customer.getId());
                    newBasket.setBasketProducts(new ArrayList<>());
                    return newBasket;
                });
        basketRepository.save(basket);
        BasketProducts basketProducts = new BasketProducts();
        basketProducts.setBasket(basket);
        basketProducts.setIdBook(selectedBook.getId());
        basketProducts.setName(selectedBook.getTitle());
        basketProducts.setAuthor(selectedBook.getAuthor());
        basketProducts.setQuantity(quantity);
        basketProducts.setPrice(selectedBook.getPrice());
        basket.getBasketProducts().add(basketProducts);

        basketProductsRepository.save(basketProducts);
        basketRepository.save(basket);

    }

    public BasketDTO findBasketDTOByUserPrincipal(Principal principal) {
        String username = principal.getName();
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        Basket basket = Optional.ofNullable(basketRepository.findBasketByUserId(customer.getId())).orElseThrow(() -> new EntityNotFoundException("Basket is empty"));
        basket.updateTotalPrice(basket);
        return mapperEntity.mapBasketToBasketDTO(basket);
    }

    public Basket findBasketByUserPrincipal(Principal principal) {
        String username = principal.getName();
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        return Optional.ofNullable(basketRepository.findBasketByUserId(customer.getId())).orElseThrow(() -> new EntityNotFoundException("Basket not exists"));
    }

    public void deleteBasketByPrincipal(Principal principal) {

        Basket basket = findBasketByUserPrincipal(principal);

        basketRepository.deleteById(basket.getIdBasket());
    }

    public Basket updateBasket(Long productId, Long quantity, Principal principal) {
        Basket basket = findBasketByUserPrincipal(principal);

        BasketProducts basketProducts = Optional.ofNullable(basketProductsRepository.findBasketProductById(productId))
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        updateBasketProductQuantity(basketProducts, quantity);
        handleZeroOrNegativeQuantity(basketProducts);
        basket.updateTotalPrice(basket);

        return basketRepository.save(basket);
    }

    private void updateBasketProductQuantity(BasketProducts basketProduct, Long quantity) {
        Book selectedBook = bookRepository.findById(basketProduct.getIdBook())
                .orElseThrow(() -> new EntityNotFoundException("Not found Book"));

        if (selectedBook.getQuantity() < quantity) {
            throw new NotEnoughBooksException("There are not enough books available");
        }
        basketProduct.setQuantity(quantity);

        basketProductsRepository.save(basketProduct);
    }

    private void handleZeroOrNegativeQuantity(BasketProducts basketProduct) {
        if (basketProduct.getQuantity() <= 0) {
            basketProductsRepository.deleteById(basketProduct.getId());
        }
    }

}





