package pl.sda.refactoring.service;

import org.junit.jupiter.api.Test;
import pl.sda.refactoring.service.DiscountService.Discount;
import pl.sda.refactoring.service.OrderSettings.DiscountSettings;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DeliveryPriceCalculatorTest {

    private final DiscountService discountService = mock(DiscountService.class);
    private final Clock fixed = Clock.fixed(Instant.parse("2022-03-04T10:00:00Z"), UTC);
    private final DiscountPriceCalculator calculator = new DiscountPriceCalculator(
        discountService,
        new OrderSettings(
            new DiscountSettings(List.of(LocalDate.now(fixed))),
            List.of()
        ),
        fixed);

    @Test
    void should_calculate_free_delivery() {
        // given
        when(discountService.getDiscount(anyString())).thenReturn(new Discount(0.1));
        final var totalPrice = new BigDecimal("200.00");
        final var deliveryPrice = new BigDecimal("20.00");

        // when
        final var discountPrice = calculator.calculateDiscountPrice(
            totalPrice,
            deliveryPrice,
            "ABC20",
            UUID.randomUUID());

        // then
        assertThat(discountPrice).isEqualTo(new BigDecimal("40.00"));
    }
}