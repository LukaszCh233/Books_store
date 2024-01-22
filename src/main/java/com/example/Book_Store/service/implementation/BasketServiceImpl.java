package com.example.Book_Store.service.implementation;

import com.example.Book_Store.controller.BasketDTO;
import com.example.Book_Store.entities.*;
import com.example.Book_Store.repository.BasketProductRepository;
import com.example.Book_Store.repository.BasketRepository;
import com.example.Book_Store.repository.BookRepository;
import com.example.Book_Store.repository.CustomerRepository;
import com.example.Book_Store.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public Basket findByIdBook(Integer id) {
        return null;
    }

    @Override
    public Basket saveBasket(Basket basket) {
        return basketRepository.save(basket);
    }

    @Override
    public void addBookToBasket(Integer idBook, Integer quantity, Principal principal) {

        String username = principal.getName();
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Book selectedBook = bookRepository.findById(idBook).orElseThrow(() -> new ResourceNotFoundException("not found Book"));
        if (selectedBook.getStatus().equals(Status.LACK)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, " Book is unavailable");
        }
        if (selectedBook.getQuantity() < quantity) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "There are no such number of books");
        }
        Basket basket = Optional.ofNullable(basketRepository.findBasketByUserId(customer.getId()))
                .orElseGet(() -> {
                    Basket newBasket = new Basket();
                    newBasket.setUserId(customer.getId());
                    newBasket.setBasketProducts(new ArrayList<>());
                    return newBasket;
                });
        BasketProducts basketProducts = new BasketProducts();
        basketProducts.setBasket(basket);
        basketProducts.setIdBook(selectedBook.getId());
        basketProducts.setName(selectedBook.getTitle());
        basketProducts.setAuthor(selectedBook.getAuthor());
        basketProducts.setQuantity(quantity);
        basketProducts.setPrice(selectedBook.getPrice() * quantity);
        basket.getBasketProducts().add(basketProducts);

        basketProductRepository.save(basketProducts);
        basketRepository.save(basket);
    }

    @Override
    public BasketDTO findBasketDTOByUserId(Principal principal) {
        String username = principal.getName();
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Basket basket = Optional.ofNullable(basketRepository.findBasketByUserId(customer.getId())).orElseThrow(() -> new ResourceNotFoundException("Basket is empty"));
        return mapBasketToBasketDTO(basket);
    }

    @Override
    public Basket findBasketByUserId(Principal principal) {
        String username = principal.getName();
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return Optional.ofNullable(basketRepository.findBasketByUserId(customer.getId())).orElseThrow(() -> new ResourceNotFoundException("Basket is empty"));
    }

    @Override
    public void deleteBasketById(Principal principal) {
       Basket basket = findBasketByUserId(principal);

        basketRepository.deleteById(basket.getIdBasket());
    }

    @Override
    public void updateBasket(Integer productId, Integer quantity, Principal principal) {

        Basket basket = findBasketByUserId(principal);

        BasketProducts updateBasketProduct = Optional.ofNullable(basketProductRepository.findBasketProductById(productId))
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        updateBasketProductQuantity(updateBasketProduct, quantity);
        handleZeroOrNegativeQuantity(updateBasketProduct);

        basketRepository.save(basket);
    }



    private void updateBasketProductQuantity(BasketProducts basketProduct, Integer quantity) {
        Book selectedBook = bookRepository.findById(basketProduct.getIdBook())
                .orElseThrow(() -> new ResourceNotFoundException("Not found Book"));

        if (selectedBook.getQuantity() < quantity) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "There are not enough books available");
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





