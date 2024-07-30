package com.example.Book_Store.order.dto;

import com.example.Book_Store.user.dto.CustomerDTO;
import com.example.Book_Store.enums.Status;

import java.time.LocalDate;
import java.util.List;


public record OrderDTO(Long id, CustomerDTO customerDTO,LocalDate localDate,Double price, Status status,List<OrderedBookDTO> orderedBooks) {
}

