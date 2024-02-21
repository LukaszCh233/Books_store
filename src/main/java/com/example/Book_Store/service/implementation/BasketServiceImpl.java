package com.example.Book_Store.service.implementation;

import com.example.Book_Store.controller.BasketDTO;
import com.example.Book_Store.entities.*;
import com.example.Book_Store.exceptions.NotEnoughBooksException;
import com.example.Book_Store.repository.BasketProductRepository;
import com.example.Book_Store.repository.BasketRepository;
import com.example.Book_Store.repository.BookRepository;
import com.example.Book_Store.repository.CustomerRepository;
import com.example.Book_Store.service.BasketService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class BasketServiceImpl implements BasketService {

    private final BasketRepository basketRepository;
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;
    private final BasketProductRepository basketProductRepository;

    @Autowired
    public BasketServiceImpl(BasketRepository basketRepository, CustomerRepository customerRepository, BookRepository bookRepository,
                             BasketProductRepository basketProductRepository) {
        this.basketRepository = basketRepository;
        this.customerRepository = customerRepository;
        this.bookRepository = bookRepository;
        this.basketProductRepository = basketProductRepository;
    }

    @Override
    public void addBookToBasket(Integer idBook, Integer quantity, Principal principal) {
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

        basketProductRepository.save(basketProducts);
        basketRepository.save(basket);

    }

    @Override
    public BasketDTO findBasketDTOByUserPrincipal(Principal principal) {
        String username = principal.getName();
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        Basket basket = Optional.ofNullable(basketRepository.findBasketByUserId(customer.getId())).orElseThrow(() -> new EntityNotFoundException("Basket is empty"));
        basket.updateTotalPrice(basket);
        return mapBasketToBasketDTO(basket);
    }

    @Override
    public Basket findBasketByUserPrincipal(Principal principal) {
        String username = principal.getName();
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        return Optional.ofNullable(basketRepository.findBasketByUserId(customer.getId())).orElseThrow(() -> new EntityNotFoundException("Basket not exists"));
    }

    @Override
    public void deleteBasketById(Principal principal) {

        Basket basket = findBasketByUserPrincipal(principal);

        basketRepository.deleteById(basket.getIdBasket());
    }

    @Override
    public void updateBasket(Integer productId, Integer quantity, Principal principal) {
        Basket basket = findBasketByUserPrincipal(principal);

        BasketProducts basketProducts = Optional.ofNullable(basketProductRepository.findBasketProductById(productId))
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        updateBasketProductQuantity(basketProducts, quantity);
        handleZeroOrNegativeQuantity(basketProducts);
        basket.updateTotalPrice(basket);

        basketRepository.save(basket);
    }

    private void updateBasketProductQuantity(BasketProducts basketProduct, Integer quantity) {
        Book selectedBook = bookRepository.findById(basketProduct.getIdBook())
                .orElseThrow(() -> new EntityNotFoundException("Not found Book"));

        if (selectedBook.getQuantity() < quantity) {
            throw new NotEnoughBooksException("There are not enough books available");
        }

        basketProduct.setQuantity(quantity);
        basketProduct.setPrice(selectedBook.getPrice() * quantity);

        basketProductRepository.save(basketProduct);
    }

    private void handleZeroOrNegativeQuantity(BasketProducts basketProduct) {
        if (basketProduct.getQuantity() <= 0) {
            basketProductRepository.deleteById(basketProduct.getId());
        }
    }

    public BasketDTO mapBasketToBasketDTO(Basket basket) {

        BasketDTO basketDTO = new BasketDTO();
        basketDTO.setIdBasket(basket.getIdBasket());
        basketDTO.setUserId(basket.getUserId());
        basketDTO.setTotalPrice(basket.getTotalPrice());
        basketDTO.setBasketProducts(basket.getBasketProducts());
        return basketDTO;
    }
}





