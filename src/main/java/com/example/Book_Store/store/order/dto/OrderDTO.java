package com.example.Book_Store.store.order.dto;

import com.example.Book_Store.account.customer.CustomerDTO;
import com.example.Book_Store.store.Status;

import java.time.LocalDate;
import java.util.List;


public record OrderDTO(Long id, CustomerDTO customer, LocalDate orderData, Double price, Status status,
                       List<OrderedBookDTO> orderedBooks) {
}

