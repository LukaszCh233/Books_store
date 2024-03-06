package com.example.Book_Store.controllerTests;

import com.example.Book_Store.config.HelpJwt;
import com.example.Book_Store.entities.Book;
import com.example.Book_Store.entities.Category;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CommonControllerTest {
    @MockBean
    CategoryServiceImpl categoryService;
    @MockBean
    BookServiceImpl bookService;
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
    void shouldGetCategories_Test() throws Exception {
        List<Category> categories = Arrays.asList(new Category(1, "testName"), new Category(2, "testName1"));

        when(categoryService.findAllCategories()).thenReturn(categories);

        mockMvc.perform(MockMvcRequestBuilders.get("/common/getCategories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("testName"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("testName1"));
    }

    @WithMockCustomer
    @Test
    void shouldGetBooks_Test() throws Exception {
        List<Book> books = Arrays.asList(new Book(1, null, null, 10, 10, null, null), new Book(2, null, null, 50, 20, null, null));

        when(bookService.findAllBooks()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/common/getBooks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].price").value(10))
                .andExpect(jsonPath("$[0].quantity").value(10))
                .andExpect(jsonPath("$[1].price").value(50))
                .andExpect(jsonPath("$[1].quantity").value(20));
    }

    @WithMockCustomer
    @Test
    void shouldGetCategoryByName_Test() throws Exception {
        Category category = new Category(1, "testName");

        when(categoryService.findCategoryByName(category.getName())).thenReturn(category);

        mockMvc.perform(MockMvcRequestBuilders.get("/common/getCategoryByName/{name}", category.getName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"testName\"}")
                        .content("{\"id\":\"1\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockCustomer
    @Test
    void shouldGetBookByTitle_Test() throws Exception {
        Book book = new Book(1, "testTitle", null, 10, 10, null, null);

        when(bookService.findByTitle(book.getTitle())).thenReturn(Collections.singletonList(book));

        mockMvc.perform(MockMvcRequestBuilders.get("/common/getBook/title/{title}", book.getTitle())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\"}")
                        .content("{\"title\":\"1testTitle\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockCustomer
    @Test
    void shouldGetBookById_Test() throws Exception {
        Book book = new Book(1, "testTitle", null, 10, 10, null, null);

        when(bookService.findBookById(book.getId())).thenReturn((book));

        mockMvc.perform(MockMvcRequestBuilders.get("/common/getBook/title/{title}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\"}")
                        .content("{\"title\":\"1testTitle\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockCustomer
    @Test
    void shouldGetBooksByCategoryName_Test() throws Exception {
        Category category = new Category(1, "testCategory");
        Book book = new Book(1, "testTitle", null, 10, 10, null, category);

        when(bookService.findByCategoryName(category.getName())).thenReturn(Collections.singletonList(book));

        mockMvc.perform(MockMvcRequestBuilders.get("/common/getBooks/categoryName/{name}", book.getCategory())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\"}")
                        .content("{\"title\":\"1testTitle\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockCustomer
    @Test
    void shouldGetBooksByCategoryId_Test() throws Exception {
        Category category = new Category(1, "testCategory");
        Book book = new Book(1, "testTitle", null, 10, 10, null, category);

        when(bookService.findByCategoryId(category.getId())).thenReturn(Collections.singletonList(book));

        mockMvc.perform(MockMvcRequestBuilders.get("/common/getBooks/categoryId/{id}", book.getCategory().getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"1\"}")
                        .content("{\"title\":\"1testTitle\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    public @interface WithMockCustomer {

    }
}
