package com.example.midterm_java;

import com.example.midterm_java.model.Product;
import com.example.midterm_java.model.Role;
import com.example.midterm_java.model.Staff;
import com.example.midterm_java.repository.ProductRepository;
import com.example.midterm_java.repository.RoleRepository;
import com.example.midterm_java.repository.StaffRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.jdbc.core.JdbcTemplate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MidtermJavaApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testRegistrationDefaultRoleStaff() throws Exception {
        String testUser = "test_staff_user_" + System.currentTimeMillis();
        mockMvc.perform(post("/register")
                        .param("username", testUser)
                        .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        Optional<Staff> registeredOpt = staffRepository.findByUserName(testUser);
        assertTrue(registeredOpt.isPresent());
        Staff staff = registeredOpt.get();
        assertEquals("STAFF", staff.getRole().getRoleName());
    }

    @Test
    void testAddToCartStockValidation() throws Exception {
        // Create a test product with stock 1
        Product testProduct = new Product();
        testProduct.setName("Test Validation Product");
        testProduct.setQty("1");
        testProduct.setPrice(10.0);
        testProduct.setExpireDate("2026-09-19");
        testProduct = productRepository.save(testProduct);
        Integer productId = testProduct.getId();

        MockHttpSession session = new MockHttpSession();

        // 1st add to cart: should succeed
        mockMvc.perform(post("/cart/add")
                        .session(session)
                        .param("productId", String.valueOf(productId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/store"))
                .andExpect(flash().attribute("toastMessage", "Item added to cart!"))
                .andExpect(flash().attribute("toastType", "success"));

        // 2nd add to cart: should exceed stock (stock is 1)
        mockMvc.perform(post("/cart/add")
                        .session(session)
                        .param("productId", String.valueOf(productId)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/store"))
                .andExpect(flash().attribute("toastMessage", "Cannot add more than available stock (1 available)!"))
                .andExpect(flash().attribute("toastType", "danger"));
    }
}




