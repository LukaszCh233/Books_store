package com.example.Book_Store.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "orderedBooks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderedBooks {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "idBook")
    private Integer idBook;
    @Column(name = "quantity")
    private Integer quantity;
}
