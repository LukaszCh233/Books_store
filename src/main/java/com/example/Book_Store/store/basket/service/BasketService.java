package com.example.Book_Store.store.basket.service;

import com.example.Book_Store.account.customer.Customer;
import com.example.Book_Store.account.customer.CustomerRepository;
import com.example.Book_Store.exceptions.NotEnoughBooksException;
import com.example.Book_Store.mapper.MapperEntity;
import com.example.Book_Store.store.Status;
import com.example.Book_Store.store.basket.dto.BasketDTO;
import com.example.Book_Store.store.basket.entity.Basket;
import com.example.Book_Store.store.basket.entity.BasketProducts;
import com.example.Book_Store.store.basket.repository.BasketProductsRepository;
import com.example.Book_Store.store.basket.repository.BasketRepository;
import com.example.Book_Store.store.book.entity.Book;
import com.example.Book_Store.store.book.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class BasketService {

    private final BasketRepository basketRepository;
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;
    private final BasketProductsRepository basketProductsRepository;
    private final MapperEntity mapperEntity;

    public BasketService(BasketRepository basketRepository, CustomerRepository customerRepository,
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
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() ->
                new EntityNotFoundException("Customer not found"));

        Book selectedBook = bookRepository.findById(idBook).orElseThrow(() ->
                new EntityNotFoundException("Book not found"));

        if (selectedBook.getStatus().equals(Status.LACK)) {
            throw new NotEnoughBooksException(" Book is unavailable" + idBook);
        }
        if (selectedBook.getQuantity() < quantity) {
            throw new NotEnoughBooksException("There are not enough copies of the book with ID: " + idBook);
        }

        Basket basket = basketRepository.findBasketByUserId(customer.getId())
                .orElseGet(() -> {
                    Basket newBasket = new Basket();
                    newBasket.setUserId(customer.getId());
                    newBasket.setBasketProducts(new ArrayList<>());
                    return basketRepository.save(newBasket);
                });

        Optional<BasketProducts> existingBasketProduct = basket.getBasketProducts().stream()
                .filter(basketProducts -> basketProducts.getIdBook().equals(idBook))
                .findFirst();
        if (existingBasketProduct.isPresent()) {
            BasketProducts basketProduct = existingBasketProduct.get();
            basketProduct.setQuantity(basketProduct.getQuantity() + quantity);
        } else {
            BasketProducts basketProduct = new BasketProducts();
            basketProduct.setBasket(basket);
            basketProduct.setIdBook(selectedBook.getId());
            basketProduct.setName(selectedBook.getTitle());
            basketProduct.setAuthor(selectedBook.getAuthor());
            basketProduct.setQuantity(quantity);
            basketProduct.setPrice(selectedBook.getPrice());
            basket.getBasketProducts().add(basketProduct);

        }
        basketRepository.save(basket);
    }

    public BasketDTO findBasketDTOByUserPrincipal(Principal principal) {
        String username = principal.getName();
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() ->
                new EntityNotFoundException("Customer not found"));

        Basket basket = basketRepository.findBasketByUserId(customer.getId()).orElseThrow(() ->
                new EntityNotFoundException("Basket not found"));

        updateTotalPrice(basket);
        return mapperEntity.mapBasketToBasketDTO(basket);
    }

    public Basket findBasketByUserPrincipal(Principal principal) {
        String username = principal.getName();
        Customer customer = customerRepository.findByEmail(username).orElseThrow(() ->
                new EntityNotFoundException("Customer not found"));

        return basketRepository.findBasketByUserId(customer.getId()).orElseThrow(() ->
                new EntityNotFoundException("Basket for customer with ID " + customer.getId() + " does not exist"));
    }

    public void deleteBasketByPrincipal(Principal principal) {
        Basket basket = findBasketByUserPrincipal(principal);

        basketRepository.deleteById(basket.getIdBasket());
    }

    @Transactional
    public Basket updateBasketProductQuantity(Long productId, Long quantity, Principal principal) {
        Basket basket = findBasketByUserPrincipal(principal);

        BasketProducts basketProduct = Optional.ofNullable(basketProductsRepository.findBasketProductById(productId))
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        Book selectedBook = bookRepository.findById(basketProduct.getIdBook())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        if (selectedBook.getQuantity() < quantity) {
            throw new NotEnoughBooksException("There are not enough books available");
        }

        if (quantity <= 0) {
            basket.getBasketProducts().remove(basketProduct);
        }
        basketProduct.setQuantity(quantity);
        updateTotalPrice(basket);

        return basketRepository.save(basket);
    }

    private void handleZeroOrNegativeQuantity(BasketProducts basketProduct) {
        if (basketProduct.getQuantity() <= 0) {
            basketProductsRepository.deleteById(basketProduct.getId());
        }
    }

    private void updateTotalPrice(Basket basket) {
        if (basket != null && basket.getBasketProducts() != null) {
            double totalPrice = basket.getBasketProducts().stream()
                    .mapToDouble(product -> product.getPrice() * product.getQuantity())
                    .sum();
            totalPrice = Math.round(totalPrice * 100.0) / 100.0;
            basket.setTotalPrice(totalPrice);
        }
    }
}





