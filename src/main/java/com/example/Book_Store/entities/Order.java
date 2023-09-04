package com.example.Book_Store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrder")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "idCustomer")
    private Customer customer;
    @Column(name = "orderData")
    private LocalDate orderData;
    @Column(name = "price")
    private Double price;
    @Column(name = "status")
    private Status status;

    public Order() {
    }

    public Order(Long id, Customer customer, LocalDate orderData, Double price, Status status) {
        this.id = id;
        this.customer = customer;
        this.orderData = orderData;
        this.price = price;
        this.status = status;
    }
}

