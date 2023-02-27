package pl.sda.cleancode.order.service;

import org.junit.jupiter.api.Test;
import pl.sda.cleancode.order.domain.*;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.sda.cleancode.order.domain.Currency.*;
import static pl.sda.cleancode.order.domain.Currency.USD;
import static pl.sda.cleancode.order.domain.WeightUnit.KG;
import static pl.sda.cleancode.order.domain.WeightUnit.LB;

final class DiscountCalculatorTest {

    private final Clock fixedClock = Clock.fixed(Instant.parse("2022-02-20T10:00:00Z"), UTC);

    private final DiscountCalculator discountCalculator = new CompositeDiscountCalculator(List.of(
        new OneTimeCouponDiscountCalculator(new TestDiscountPort()),
        new FreeDeliveryDiscountCalculator(
            List.of(LocalDate.now(fixedClock)),
            fixedClock
        )
    ));

    @Test
    void should_calculate_discount() {
        // given discount context with 20% coupon & free delivery day
        final var discountContext = new DiscountCalculator.DiscountContext(
            CustomerId.of("9c74dd58-83e3-4a21-8220-bec2ddcd590a"),
            new ExchangedOrderItemList(
                USD,
                new ExchangedOrderItem(
                    new OrderItem(
                        ProductId.of("abeffc80-71b8-43d0-9b57-bdda6f963625"),
                        Price.of("24.50", EUR),
                        Weight.of(0.20, LB),
                        Quantity.of(1)),
                    Price.of("25.34", USD)
                ),
                new ExchangedOrderItem(
                    new OrderItem(
                        ProductId.of("6e6a0cba-37a6-4c88-a098-710e2823c549"),
                        Price.of("32.50", PLN),
                        Weight.of(0.12, KG),
                        Quantity.of(3)
                    ),
                    Price.of("143.47", USD)
                )),
            Price.of("20.00", USD),
            DiscountCoupon.of("ABC20")
        );

        // when discount is calculated
        final var discountContextResult = discountCalculator.calculate(discountContext);

        // then discount should be 91.15 (20%) USD + 20 USD (free delivery) = 111.15 USD
        assertThat(discountContextResult.discount()).isEqualTo(Price.of("111.15", USD));
    }
}
