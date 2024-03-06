package com.example.Book_Store.controllerTests;

import com.example.Book_Store.config.HelpJwt;
import com.example.Book_Store.controller.BasketDTO;
import com.example.Book_Store.entities.Basket;
import com.example.Book_Store.entities.BasketProducts;
import com.example.Book_Store.entities.Order;
import com.example.Book_Store.service.implementation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomerControllerTest {

    @MockBean
    OrderServiceImpl orderService;
    @MockBean
    BasketServiceImpl basketService;
    @Autowired
    WebApplicationContext context;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @WithMockCustomer
    @Test
    void shouldAddToBasket_Test() throws Exception {
        Principal principal = mock(Principal.class);

        Basket basket = new Basket(100.0, 1, 1, null);
        BasketProducts basketProducts = new BasketProducts(1, basket, 1, "testName", "testAuthor", 10.0, 10);

        doNothing().when(basketService).addBookToBasket(basketProducts.getIdBook(), 10, principal);

        mockMvc.perform(post("/customer/toBasket/{idBook}/{quantity}", basketProducts.getIdBook(), 10)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Book added to basket"));
    }

    @WithMockCustomer
    @Test
    void shouldOrder_Test() throws Exception {
        Principal principal = mock(Principal.class);
        Order order = new Order(1, null, LocalDate.now(), 100.0, null, null);

        when(orderService.saveOrder(order, principal)).thenReturn(order);

        mockMvc.perform(post("/customer/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Order is completed"));
    }

    @WithMockCustomer
    @Test
    void shouldDeleteBasket_test() throws Exception {
        Principal principal = mock(Principal.class);

        doNothing().when(basketService).deleteBasketByPrincipal(principal);

        mockMvc.perform(MockMvcRequestBuilders.delete("/customer/deleteBasket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Basket deleted successfully"));
    }

    @WithMockCustomer
    @Test
    void shouldDisplayBasket_Test() throws Exception {

        BasketDTO mockBasketDTO = new BasketDTO();
        mockBasketDTO.setIdBasket(1);
        mockBasketDTO.setBasketProducts(new ArrayList<>());
        mockBasketDTO.setTotalPrice(100.0);
        mockBasketDTO.setUserId(1);

        when(basketService.findBasketDTOByUserPrincipal(any(Principal.class))).thenReturn(mockBasketDTO);


        mockMvc.perform(MockMvcRequestBuilders.get("/customer/getBasket")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPrice").value("100.0"));
    }

    @WithMockCustomer
    @Test
    void shouldUpdateBasket_Test() throws Exception {
        Principal principal = mock(Principal.class);
        Basket basket = new Basket();
        BasketProducts basketProducts = new BasketProducts(1, basket, 1, null, null, 10.0, 10);

        doNothing().when(basketService).updateBasket(basketProducts.getId(), 5, principal);

        mockMvc.perform(put("/customer/updateBasket/{id}/{quantity}", 1, 5)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantity\":\"5\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    public @interface WithMockCustomer {

    }
}



