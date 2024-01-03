package com.example.Book_Store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrder")
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "idCustomer")
    private Customer customer;
    @Column(name = "orderData")
    private LocalDate orderData;
    @Column(name = "price")
    private Double price;
    @Column(name = "status")
    private Status status;
    @OneToMany(mappedBy = "order", orphanRemoval = true)
    private List<OrderedBooks> orderedBooks;
}

