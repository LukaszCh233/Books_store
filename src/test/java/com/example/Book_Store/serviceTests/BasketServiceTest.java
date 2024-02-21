package com.example.Book_Store.serviceTests;

import com.example.Book_Store.entities.*;
import com.example.Book_Store.repository.BasketProductRepository;
import com.example.Book_Store.repository.BasketRepository;
import com.example.Book_Store.repository.BookRepository;
import com.example.Book_Store.repository.CustomerRepository;
import com.example.Book_Store.service.implementation.BasketServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Principal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BasketServiceTest {

    @Mock
    private BasketRepository basketRepository;
    @InjectMocks
    private BasketServiceImpl basketService;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BasketProductRepository basketProductRepository;

    @Test
    void shouldAddBookToBasket_Successfully_Test() {
        Integer idBook = 1;
        Integer quantity = 3;
        Principal principal = Mockito.mock(Principal.class);

        when(principal.getName()).thenReturn("testEmail");

        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(1, "TestName", "TestLastName", customerLogin, 123456, null);
        Category category = new Category(1, "testCategory");
        Book selectedBook = new Book(1, "Test Book", "Test Author", 10.0, 10, Status.AVAILABLE, category);

        when(customerRepository.findByEmail("testEmail")).thenReturn(Optional.of(customer));
        when(bookRepository.findById(idBook)).thenReturn(Optional.of(selectedBook));
        when(basketRepository.findBasketByUserId(customer.getId())).thenReturn(null);

        assertDoesNotThrow(() -> basketService.addBookToBasket(idBook, quantity, principal));

        verify(customerRepository, times(1)).findByEmail("testEmail");
        verify(bookRepository, times(1)).findById(idBook);
        verify(basketRepository, times(2)).save(Mockito.any(Basket.class));
        verify(basketProductRepository, times(1)).save(Mockito.any(BasketProducts.class));
    }

    @Test
    void shouldFindBasketByUserId_Successfully() {

        Principal principal = Mockito.mock(Principal.class);
        when(principal.getName()).thenReturn("testEmail");

        CustomerLogin customerLogin = new CustomerLogin("testEmail", "testPassword");
        Customer customer = new Customer(1, "TestName", "TestLastName", customerLogin, 123456, null);

        Basket basket = new Basket(100.0, 1, customer.getId(), null);

        when(customerRepository.findByEmail("testEmail")).thenReturn(Optional.of(customer));
        when(basketRepository.findBasketByUserId(customer.getId())).thenReturn(basket);


        Basket resultBasket = basketService.findBasketByUserPrincipal(principal);

        verify(customerRepository, times(1)).findByEmail("testEmail");
        verify(basketRepository, times(1)).findBasketByUserId(customer.getId());

        assertNotNull(resultBasket);
        assertEquals(customer.getId(), resultBasket.getUserId());
    }

    @Test
    void shouldDeleteBasketSuccessfully() {
        String userEmail = "testEmail";
        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn(userEmail);

        Customer customer = new Customer(1, "TestName", "TestLastName", new CustomerLogin(userEmail, "testPassword"), 123456, null);
        when(customerRepository.findByEmail(userEmail)).thenReturn(Optional.of(customer));

        Basket basket = new Basket(100.0, 1, customer.getId(), null);
        when(basketRepository.findBasketByUserId(customer.getId())).thenReturn(basket);

        basketService.deleteBasketById(principal);

        verify(customerRepository, times(1)).findByEmail(userEmail);
        verify(basketRepository, times(1)).findBasketByUserId(customer.getId());
        verify(basketRepository, times(1)).deleteById(basket.getIdBasket());
    }

    @Test
    void shouldUpdateBasketProduct_Test() {

        Principal principal = mock(Principal.class);
        when(principal.getName()).thenReturn("userEmail");
        Customer customer = new Customer(1, "TestName", "TestLastName", new CustomerLogin("userEmail", "testPassword"), 123456, null);

        when(customerRepository.findByEmail("userEmail")).thenReturn(Optional.of(customer));
        Basket basket = new Basket();
        when(basketRepository.findBasketByUserId(customer.getId())).thenReturn(basket);

        BasketProducts basketProducts = new BasketProducts(1, null, 1, null, null, 20.0, 2);
        when(basketProductRepository.findBasketProductById(1)).thenReturn(basketProducts);

        Book selectedBook = new Book(1, null, null, 10.0, 100, Status.AVAILABLE, null);

        when(bookRepository.findById(basketProducts.getIdBook())).thenReturn(Optional.of(selectedBook));

        basketService.updateBasket(1, 3, principal);

        assertEquals(3, basketProducts.getQuantity());
        assertEquals(30.0, basketProducts.getPrice());

        verify(basketProductRepository, times(1)).save(basketProducts);
        verify(basketRepository, times(1)).save(basket);
    }
}



