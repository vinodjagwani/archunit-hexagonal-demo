package com.example.orders.adapter.rest;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.assertj.MvcTestResult;

/** Full-context test: proves the composition root wires HTTP adapter -> port -> domain -> driven adapter. */
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    private final MockMvcTester mockMvc;

    @Autowired
    OrderControllerTest(final MockMvcTester mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("POST /orders with a valid order returns 201 and the created order")
    void testPlaceOrderReturnsCreated() {
        final MvcTestResult result = postOrder(anOrderJson("book", 2));

        assertThat(result).hasStatus(HttpStatus.CREATED)
                .bodyJson().extractingPath("$.product").isEqualTo("book");
    }

    @Test
    @DisplayName("POST /orders with a non-positive quantity returns 400 with a problem detail")
    void testPlaceOrderRejectsInvalidQuantity() {
        final MvcTestResult result = postOrder(anOrderJson("book", 0));

        assertThat(result).hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson().extractingPath("$.detail").isEqualTo("quantity must be positive");
    }

    private MvcTestResult postOrder(final String json) {
        return mockMvc.post().uri("/orders").contentType(MediaType.APPLICATION_JSON).content(json).exchange();
    }

    private static String anOrderJson(final String product, final int quantity) {
        return """
                {"product": "%s", "quantity": %d}
                """.formatted(product, quantity);
    }
}
