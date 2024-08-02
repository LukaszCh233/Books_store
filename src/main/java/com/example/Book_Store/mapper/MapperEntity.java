package com.example.Book_Store.mapper;

import com.example.Book_Store.basket.dto.BasketDTO;
import com.example.Book_Store.basket.dto.BasketProductsDTO;
import com.example.Book_Store.basket.entity.Basket;
import com.example.Book_Store.basket.entity.BasketProducts;
import com.example.Book_Store.order.dto.OrderDTO;
import com.example.Book_Store.order.dto.OrderedBookDTO;
import com.example.Book_Store.order.entity.Order;
import com.example.Book_Store.order.entity.OrderedBooks;
import com.example.Book_Store.user.dto.AdminDTO;
import com.example.Book_Store.user.dto.CustomerDTO;
import com.example.Book_Store.user.entity.Admin;
import com.example.Book_Store.user.entity.Customer;
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
                customer.getCustomerLogin().getEmail(), customer.getNumber());

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
}
