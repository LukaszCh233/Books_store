package com.example.Book_Store.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCustomer")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "lastName")
    private String lastName;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "email")
    private CustomerLogin customerLogin;
    @Column(name = "number")
    private int number;

    public Customer() {
    }

    public Customer(Long id, String name, String lastName, CustomerLogin customerLogin, int number) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.customerLogin = customerLogin;
        this.number = number;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", customerLogin=" + customerLogin +
                ", number=" + number +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return number == customer.number && Objects.equals(id, customer.id) && Objects.equals(name, customer.name) && Objects.equals(lastName, customer.lastName) && Objects.equals(customerLogin, customer.customerLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, customerLogin, number);
    }
}

