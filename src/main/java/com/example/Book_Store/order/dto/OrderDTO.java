package com.example.Book_Store.order.dto;

import com.example.Book_Store.enums.Status;
import com.example.Book_Store.user.dto.CustomerDTO;

import java.time.LocalDate;
import java.util.List;


public record OrderDTO(Long id, CustomerDTO customer, LocalDate orderData, Double price, Status status,
                       List<OrderedBookDTO> orderedBooks) {
}

