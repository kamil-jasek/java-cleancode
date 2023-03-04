package pl.sda.refactoring.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.sda.refactoring.service.CurrencyService;
import pl.sda.refactoring.service.CustomerService;
import pl.sda.refactoring.service.DiscountService;
import pl.sda.refactoring.service.DiscountService.Discount;
import pl.sda.refactoring.service.EmailService;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.matchesRegex;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CurrencyService currencyService;

    @MockBean
    private DiscountService discountService;

    @MockBean
    private EmailService emailService;

    @Test
    @SneakyThrows
    void test_make_order() {
        // given customer exists
        when(customerService.exists(any())).thenReturn(true);
        // given baseCurrency service working
        when(currencyService.exchange(any(), any(), any())).thenReturn(new BigDecimal("20.00"));
        // given discount coupon exists
        when(discountService.getDiscount(any())).thenReturn(new Discount(0.1));

        // when post request is performed
        mvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "customerId": "9c74dd58-83e3-4a21-8220-bec2ddcd590c",
                      "orderCurrency": "USD",
                      "orderItems": [
                        {
                          "productId": "87b899d8-3c87-4889-80e2-34b7d4ac0f53",
                          "price": "24.12",
                          "currency": "EUR",
                          "weight": 0.12,
                          "weightUnit": "KG",
                          "quantity": 2
                        }
                      ],
                      "discountCoupon": "ABC20"
                    }
                    """))
            // then expect response 201 and location header
            .andExpect(status().isCreated())
            .andExpect(header().string(
                "location",
                matchesRegex("/api/orders/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}")));
    }

    @Test
    @SneakyThrows
    void test_customer_not_exists() {
        // given customer not exists
        when(customerService.exists(any())).thenReturn(false);

        // when post request is performed
        mvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "customerId": "9c74dd58-83e3-4a21-8220-bec2ddcd590c",
                      "orderCurrency": "USD",
                      "orderItems": [
                        {
                          "productId": "87b899d8-3c87-4889-80e2-34b7d4ac0f53",
                          "price": "24.12",
                          "currency": "EUR",
                          "weight": 0.12,
                          "weightUnit": "KG",
                          "quantity": 2
                        }
                      ],
                      "discountCoupon": "ABC20"
                    }
                    """))
            // then expect response 201 and location header
            .andExpect(status().isBadRequest())
            .andExpect(content().json("""
                {
                  "message": "customer not found 9c74dd58-83e3-4a21-8220-bec2ddcd590c"
                }
                """));
    }

    @Test
    @SneakyThrows
    void test_invalid_order_parameters() {
        // when post request is performed
        mvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "customerId": "???",
                      "orderCurrency": "???",
                      "orderItems": [
                      ]
                    }
                    """))
            // then expect response 201 and location header
            .andExpect(status().isBadRequest())
            .andExpect(content().json("""
                {
                  "errors": [
                    "orderCurrency: must match \\"USD|EUR|PLN\\"",
                    "orderItems: must not be empty",
                    "customerId: must be a valid UUID"
                  ]
                }
                """));
    }

    @Test
    @SneakyThrows
    void test_invalid_order_item_parameters() {
        // when post request is performed
        mvc.perform(post("/api/orders")
                .contentType(APPLICATION_JSON)
                .content("""
                    {
                      "customerId": "9c74dd58-83e3-4a21-8220-bec2ddcd590c",
                      "orderCurrency": "USD",
                      "orderItems": [
                        {
                          "productId": "???",
                          "price": "-0.1",
                          "currency": "???",
                          "weight": -0.1,
                          "weightUnit": "???",
                          "quantity": 0
                        }
                      ],
                      "discountCoupon": "ABC20"
                    }
                    """))
            // then expect response 201 and location header
            .andExpect(status().isBadRequest())
            .andExpect(content().json("""
                {
                  "errors": [
                    "orderItems[0].quantity: must be greater than or equal to 1",
                    "orderItems[0].currency: must match \\"USD|EUR|PLN\\"",
                    "orderItems[0].price: must be greater than or equal to 0",
                    "orderItems[0].weight: must be greater than or equal to 0",
                    "orderItems[0].weightUnit: must match \\"KG|GM|LB\\"",
                    "orderItems[0].productId: must be a valid UUID"
                  ]
                }
                """));
    }
}