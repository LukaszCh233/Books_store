package com.example.Book_Store.controllerTests;

import com.example.Book_Store.config.HelpJwt;
import com.example.Book_Store.controller.CustomerDTO;
import com.example.Book_Store.controller.OrderDTO;
import com.example.Book_Store.entities.*;
import com.example.Book_Store.service.implementation.BookServiceImpl;
import com.example.Book_Store.service.implementation.CategoryServiceImpl;
import com.example.Book_Store.service.implementation.CustomerServiceImpl;
import com.example.Book_Store.service.implementation.OrderServiceImpl;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AdministratorControllerTest {

    @MockBean
    CategoryServiceImpl categoryService;
    @MockBean
    BookServiceImpl bookService;
    @MockBean
    CustomerServiceImpl customerService;
    @MockBean
    OrderServiceImpl orderService;
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

    @WithMockAdmin
    @Test
    void shouldCreateCategory_Test() throws Exception {
        Category category = new Category();
        category.setName("Example Category");

        when(categoryService.createCategory(category)).thenReturn(category);

        mockMvc.perform(post("/admin/addCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Example Category\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Example Category"));
    }

    @WithMockAdmin
    @Test
    void shouldGetCategoryById_Test() throws Exception {
        Category category = new Category(1, "testCategory");

        when(categoryService.findCategoryById(category.getId())).thenReturn(category);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/getCategory/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("testCategory"));
    }

    @WithMockAdmin
    @Test
    void shouldUpdateCategory_Test() throws Exception {
        Category category = new Category(1, "testCategory");
        Category updateCategory = new Category(null, "updateTestCategory");

        when(categoryService.updateCategory(category.getId(), updateCategory)).thenReturn(updateCategory);

        mockMvc.perform(put("/admin/updateCategory/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"updateTestCategory\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockAdmin
    @Test
    void shouldDeleteCategoryById_Test() throws Exception {
        Category category = new Category(1, "testCategory");

        doNothing().when(categoryService).deleteCategoryById(category.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/deleteCategory/{idCategory}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Category deleted"));
    }

    @WithMockAdmin
    @Test
    void shouldDeleteAllCategories_Test() throws Exception {
        List<Category> categories = Arrays.asList(new Category(1, "testName"), new Category(2, "testName1"));

        doNothing().when(categoryService).deleteAllCategories();

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/deleteCategories"))
                .andExpect(status().isOk())
                .andExpect(content().string("All categories have been deleted"));
    }

    @WithMockAdmin
    @Test
    void shouldCreateBook_Test() throws Exception {
        Category category = new Category(1, "testCategory");

        Book book = new Book(1, "testTitle", "testAuthor", 10.0, 10, Status.AVAILABLE, category);

        when(bookService.createBook(any())).thenReturn(book);

        mockMvc.perform(post("/admin/addBook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"testTitle\",\"author\":\"testAuthor\",\"price\":10.0,\"quantity\":10,\"status\":\"AVAILABLE\",\"category\":{\"id\":1,\"name\":\"testCategory\"}}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("testTitle"))
                .andExpect(jsonPath("$.author").value("testAuthor"))
                .andExpect(jsonPath("$.price").value(10.0))
                .andExpect(jsonPath("$.quantity").value(10))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andExpect(jsonPath("$.category.id").value(1))
                .andExpect(jsonPath("$.category.name").value("testCategory"));
    }

    @WithMockAdmin
    @Test
    void shouldDeleteBookById_Test() throws Exception {
        Category category = new Category(1, "testCategory");
        Book book = new Book(1, "testTitle", "testAuthor", 10.0, 10, Status.AVAILABLE, category);

        doNothing().when(bookService).deleteBookById(book.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/deleteBook/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Book deleted"));
    }

    @WithMockAdmin
    @Test
    void shouldDeleteBookByTitle_Test() throws Exception {
        Category category = new Category(1, "testCategory");
        Book book = new Book(1, "testTitle", "testAuthor", 10.0, 10, Status.AVAILABLE, category);

        doNothing().when(bookService).deleteBookByTitle(book.getTitle());

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/deleteBook/title/{title}", "testTitle"))
                .andExpect(status().isOk())
                .andExpect(content().string("Book deleted"));
    }

    @WithMockAdmin
    @Test
    void shouldDeleteAllBooks_Test() throws Exception {
        List<Book> books = Arrays.asList(new Book(1, "test", "test", 10.0, 10, Status.AVAILABLE, new Category(1, "testCategory")), new Book(2, "test1", "test2", 10.0, 10, Status.AVAILABLE, new Category(1, "testCategory")));

        doNothing().when(bookService).deleteAllBooks();

        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/deleteBooks"))
                .andExpect(status().isOk())
                .andExpect(content().string("Books deleted"));
    }

    @WithMockAdmin
    @Test
    void shouldUpdateBookById_Test() throws Exception {
        Category category = new Category(1, "testCategory");
        Book book = new Book(1, "testTitle", "testAuthor", 10.0, 10, Status.AVAILABLE, category);
        Book updateBook = new Book(null, "updateTitle", "updateAuthor", 20.0, 20, Status.AVAILABLE, category);
        when(bookService.updateBook(book.getId(), updateBook)).thenReturn(updateBook);

        mockMvc.perform(put("/admin/updateBook/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"updateTitle\"}")
                        .content("{\"author\":\"updateAuthor\"}")
                        .content("{\"price\":\"20.0\"}")
                        .content("{\"quantity\":\"20\"}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @WithMockAdmin
    @Test
    void shouldGetAllCustomers_Test() throws Exception {
        List<CustomerDTO> customers = Arrays.asList(new CustomerDTO(), new CustomerDTO());

        when(customerService.findAllCustomers()).thenReturn(customers);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/getCustomers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @WithMockAdmin
    @Test
    void shouldGetOrderById_Test() throws Exception {
        OrderDTO order = new OrderDTO();
        order.setId(1);
        order.setPrice(100.0);

        when(orderService.findOrderById(order.getId())).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/getOrder/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.price").value("100.0"));
    }

    @WithMockAdmin
    @Test
    void shouldGetAllOrders_Test() throws Exception {
        List<OrderDTO> orders = Arrays.asList(new OrderDTO(), new OrderDTO());

        when(orderService.findAllOrders()).thenReturn(orders);

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/getOrders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @WithMockAdmin
    @Test
    void shouldSendOrder_Test() throws Exception {
        Order order = new Order(1, new Customer(), LocalDate.now(), 10.0, Status.ORDERED, null);
        Order sentOrder = new Order(1, new Customer(), LocalDate.now(), 10.0, Status.SENT, null);

        when(orderService.updateOrderStatus(order.getId())).thenReturn(sentOrder);

        mockMvc.perform(put("/admin/sendOrder/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public @interface WithMockAdmin {

    }
}

