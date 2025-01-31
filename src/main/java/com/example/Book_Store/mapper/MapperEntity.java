package com.example.Book_Store.mapper;

import com.example.Book_Store.account.admin.Admin;
import com.example.Book_Store.account.admin.AdminDTO;
import com.example.Book_Store.account.customer.Customer;
import com.example.Book_Store.account.customer.CustomerDTO;
import com.example.Book_Store.store.basket.dto.BasketDTO;
import com.example.Book_Store.store.basket.dto.BasketProductsDTO;
import com.example.Book_Store.store.basket.entity.Basket;
import com.example.Book_Store.store.basket.entity.BasketProducts;
import com.example.Book_Store.store.book.dto.BookDTO;
import com.example.Book_Store.store.book.entity.Book;
import com.example.Book_Store.store.bookCategory.dto.BookCategoryDTO;
import com.example.Book_Store.store.bookCategory.entity.BookCategory;
import com.example.Book_Store.store.order.dto.OrderDTO;
import com.example.Book_Store.store.order.dto.OrderedBookDTO;
import com.example.Book_Store.store.order.entity.Order;
import com.example.Book_Store.store.order.entity.OrderedBooks;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperEntity {
    public AdminDTO mapAdminToAdminDTO(Admin admin) {

        return new AdminDTO(admin.getId(), admin.getName(), admin.getEmail());
    }

    public BasketDTO mapBasketToBasketDTO(Basket basket) {
        List<BasketProductsDTO> basketProductsDTOList = mapBasketProductsToBasketProductsDTO(basket.getBasketProducts());

        return new BasketDTO(basket.getIdBasket(), basket.getUserId(), basket.getTotalPrice(), basketProductsDTOList);
    }

    public List<BasketProductsDTO> mapBasketProductsToBasketProductsDTO(List<BasketProducts> basketProductsList) {

        return basketProductsList.stream()
                .map(basketProducts -> new BasketProductsDTO(
                        basketProducts.getId(),
                        basketProducts.getIdBook(),
                        basketProducts.getName(),
                        basketProducts.getAuthor(),
                        basketProducts.getPrice(),
                        basketProducts.getQuantity()))
                .collect(Collectors.toList());
    }

    public CustomerDTO mapCustomerToCustomerDTO(Customer customer) {
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getLastName(),
                customer.getEmail(), customer.getNumber());
    }

    public List<CustomerDTO> mapCustomersToCustomersDTO(List<Customer> customers) {
        return customers.stream()
                .map(this::mapCustomerToCustomerDTO)
                .collect(Collectors.toList());
    }

    public OrderedBookDTO mapOrderedBookToOrderedBookDTO(OrderedBooks orderedBook) {
        return new OrderedBookDTO(orderedBook.getIdBook(), orderedBook.getQuantity());
    }

    public List<OrderedBookDTO> mapOrderedBooksToOrderedBooksDTO(List<OrderedBooks> orderedBooks) {
        return orderedBooks.stream()
                .map(this::mapOrderedBookToOrderedBookDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO mapOrderToOrderDTO(Order order) {
        List<OrderedBookDTO> orderedBookDTOList = mapOrderedBooksToOrderedBooksDTO(order.getOrderedBooks());
        CustomerDTO customerDTO = mapCustomerToCustomerDTO(order.getCustomer());

        return new OrderDTO(order.getId(), customerDTO, LocalDate.now(), order.getPrice(), order.getStatus(), orderedBookDTOList);
    }

    public List<OrderDTO> mapOrdersToOrdersDTO(List<Order> orderList) {
        return orderList.stream()
                .map(this::mapOrderToOrderDTO)
                .collect(Collectors.toList());
    }

    public BookCategoryDTO mapBookCategoryToBookCategoryDTO(BookCategory bookCategory) {
        return new BookCategoryDTO(bookCategory.getName());
    }

    public List<BookCategoryDTO> mapBookCategoryListToBookCategoryListDTO(List<BookCategory> bookCategoryList) {
        return bookCategoryList.stream()
                .map(this::mapBookCategoryToBookCategoryDTO)
                .collect(Collectors.toList());
    }

    public BookDTO mapBookToBookDTO(Book book) {
        return new BookDTO(book.getTitle(), book.getAuthor(), book.getPrice(), book.getQuantity(),
                book.getBookCategory().getName(), book.getStatus());
    }

    public List<BookDTO> mapBookListToBookListDTO(List<Book> bookList) {
        return bookList.stream()
                .map(this::mapBookToBookDTO)
                .collect(Collectors.toList());
    }
}
