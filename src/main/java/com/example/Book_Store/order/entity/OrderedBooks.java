package com.example.Book_Store.order.entity;

import com.example.Book_Store.order.entity.Order;
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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "idBook")
    private Long idBook;
    @Column(name = "quantity")
    private Long quantity;
}
