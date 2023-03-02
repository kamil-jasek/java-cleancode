package pl.sda.refactoring.controller;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pl.sda.refactoring.entity.*;
import pl.sda.refactoring.service.*;
import pl.sda.refactoring.service.DiscountService.Discount;
import pl.sda.refactoring.service.OrderSettings.DiscountSettings;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private final DiscountService discountService = mock(DiscountService.class);
    private final CurrencyService currencyService = mock(CurrencyService.class);
    private final CustomerService customerService = mock(CustomerService.class);
    private final OrderRepo orderRepo = mock(OrderRepo.class);
    private final EmailService emailService = mock(EmailService.class);
    private final OrderSettings orderSettings = new OrderSettings(
        new DiscountSettings(List.of(LocalDate.now())),
        List.of("test@test.pl"));

    private final OrderService orderService = new OrderService(
        discountService,
        currencyService,
        customerService,
        orderRepo,
        emailService,
        orderSettings);

    @Test
    void should_make_order() {
        // given
        UUID customerId = UUID.fromString("f363c254-6bc3-440b-b120-4870999da0d9");
        when(customerService.exists(customerId)).thenReturn(true);
        when(currencyService.exchange(any(), any(), any())).thenReturn(new BigDecimal("52.00"));
        when(discountService.getDiscount(any())).thenReturn(new Discount(0.1));

        // when make order is executed
        UUID id = orderService.makeOrder(
            customerId,
            List.of(OrderItem.builder()
                .productId(UUID.fromString("50df82ab-f553-4c53-83c2-0bf993ffaab9"))
                .price(new BigDecimal("12.00"))
                .currency(Currency.PLN)
                .weight(0.2)
                .weightUnit(WeightUnit.KG)
                .quantity(2)
                .build()),
            Currency.USD,
            "ABC20");

        // then
        assertThat(id).isNotNull();
        var orderCapture = ArgumentCaptor.forClass(Order.class);
        verify(orderRepo).save(orderCapture.capture());
        var order = orderCapture.getValue();
        assertThat(order).usingRecursiveComparison()
            .ignoringFields("ctime", "items.id")
            .isEqualTo(Order.builder()
                .id(order.id())
                .customerId(customerId)
                .status(OrderStatus.CONFIRMED)
                .currency(Currency.USD)
                .items(List.of(
                    OrderItem.builder()
                        .productId(UUID.fromString("50df82ab-f553-4c53-83c2-0bf993ffaab9"))
                        .price(new BigDecimal("12.00"))
                        .currency(Currency.PLN)
                        .weight(0.2)
                        .weightUnit(WeightUnit.KG)
                        .exchPrice(new BigDecimal("52.00"))
                        .quantity(2)
                        .build()
                ))
                .discount(new BigDecimal("25.40"))
                .delivery(new BigDecimal("15.00"))
                .totalExch(new BigDecimal("104.00"))
                .discountedTotal(new BigDecimal("93.60"))
                .build());
        verify(emailService).send(any(), any(), any());
    }
}
