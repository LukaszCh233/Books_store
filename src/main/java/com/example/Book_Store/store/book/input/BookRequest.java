package com.example.Book_Store.store.book.input;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookRequest {
    @NotNull(message = "Title cannot be null")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    private String title;

    @NotNull(message = "Author cannot be null")
    private String author;

    @NotNull(message = "Price cannot be null")
    private Double price;

    @NotNull(message = "Quantity cannot be null")
    private Long quantity;

    @NotNull(message = "Category ID cannot be null")
    private Long categoryId;
}
