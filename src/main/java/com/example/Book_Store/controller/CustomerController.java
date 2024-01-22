package com.example.Book_Store.controller;

import ch.qos.logback.classic.Logger;
import com.example.Book_Store.service.implementation.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;


@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final BookServiceImpl bookService;
    private final CustomerServiceImpl customerService;
    private final OrderServiceImpl orderService;
    private final BasketServiceImpl basketService;
    private final BasketProductServiceImpl basketProductService;
    private final OrderedBooksServiceImpl orderedBooksService;
    private final Logger logger = (Logger) LoggerFactory.getLogger(CustomerController.class);


    @Autowired
    public CustomerController(BookServiceImpl bookService, CustomerServiceImpl customerService, OrderServiceImpl orderService,
                              BasketServiceImpl basketService, BasketProductServiceImpl basketProductService, OrderedBooksServiceImpl orderedBooksService) {
        this.bookService = bookService;
        this.customerService = customerService;
        this.orderService = orderService;
        this.basketService = basketService;
        this.basketProductService = basketProductService;
        this.orderedBooksService = orderedBooksService;
    }

    @PostMapping("/toBasket/{idBook}/{quantity}")
    public ResponseEntity<String> addBookToBasket(@PathVariable Integer idBook, @PathVariable Integer quantity, Principal principal) {

        basketService.addBookToBasket(idBook, quantity, principal);
        return ResponseEntity.ok("book added to basket");
    }


    /*  @PostMapping("/order")
      public ResponseEntity<?> orderBooks(Principal principal) {

          String username = principal.getName();
          Customer customer = customerService.findByEmail(username)
                  .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
          Basket basket = Optional.ofNullable(basketService.findBasketByUserId(customer.getId()))
          .orElseThrow(() -> new ResourceNotFoundException("Basket does not exist for the customer"));

          if (!basket.getBasketProducts().isEmpty()) {
              Order order = new Order();
              order.setCustomer(customer);
              order.setOrderData(LocalDate.now());
              order.setOrderedBooks(new ArrayList<>());

              double totalPrice = basket.getBasketProducts().stream().mapToDouble(BasketProducts::getPrice)
                      .sum();
              order.setPrice(totalPrice);
              orderService.saveOrder(order);

              List<OrderedBooks> orderedBooksList = basket.getBasketProducts().stream().map(basketProducts -> {
                          OrderedBooks orderedBooks = new OrderedBooks();
                          orderedBooks.setOrder(order);
                          orderedBooks.setIdBook(basketProducts.getIdBook());
                          orderedBooks.setQuantity(basketProducts.getQuantity());
                          return orderedBooks;
                      })
                      .collect(Collectors.toList());
              orderedBooksService.saveOrderedBooks(orderedBooksList);
              order.setOrderedBooks(orderedBooksList);
              order.setStatus(Status.ORDERED);
              orderService.saveOrder(order);
              for (OrderedBooks orderedBooks : orderedBooksList) {
                  Book book = bookService.findBookById(orderedBooks.getIdBook());
                  int newQuantity = book.getQuantity() - orderedBooks.getQuantity();
                  book.setQuantity(newQuantity);
                  bookService.updateBook(book);
              }
              basketService.deleteBasketById(basket.getIdBasket());
          }
          return ResponseEntity.ok("order is completed");
      }

     */
    @DeleteMapping("/deleteBasket")
    public ResponseEntity<?> clearBasket(Principal principal) {

        basketService.deleteBasketById(principal);
        return ResponseEntity.ok("Basket deleted successfully");
    }

    @GetMapping("/getBasket")
    public ResponseEntity<?> displayBasket(Principal principal) {

        BasketDTO basketDTO = basketService.findBasketDTOByUserId(principal);
        return ResponseEntity.ok(basketDTO);
    }

    @PutMapping("/updateBasket/{id}/{quantity}")
    public ResponseEntity<?> updateBasket(@PathVariable Integer id, @PathVariable Integer quantity, Principal principal) {
        basketService.updateBasket(id, quantity, principal);
        return ResponseEntity.ok("Quantity update");
    }
}
