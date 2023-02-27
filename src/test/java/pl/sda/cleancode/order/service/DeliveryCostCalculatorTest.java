package pl.sda.cleancode.order.service;

import org.junit.jupiter.api.Test;
import pl.sda.cleancode.order.service.DeliveryCostSettings.DeliveryCostLevel;
import pl.sda.cleancode.order.domain.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.sda.cleancode.order.domain.Currency.PLN;
import static pl.sda.cleancode.order.domain.Currency.USD;
import static pl.sda.cleancode.order.domain.WeightUnit.KG;

class DeliveryCostCalculatorTest {

    private final DeliveryCostCalculator calculator = new DeliveryCostCalculator(
        new DeliveryCostSettings(Price.of("20.00", USD), List.of(
            new DeliveryCostLevel(Price.of("400.00", USD), Weight.of(2, KG), Price.of("0.00", USD)),
            new DeliveryCostLevel(Price.of("200.00", USD), Weight.of(1, KG), Price.of("12.00", USD)),
            new DeliveryCostLevel(Price.of("100.00", USD), Weight.of(1, KG), Price.of("15.00", USD))
        )),
        new TestExchangeCurrencyPort());

    @Test
    void should_calculate_15_usd_delivery_cost() {
        // given order items
        final var orderItems = new ExchangedOrderItemList(USD,
            new ExchangedOrderItem(new OrderItem(
                ProductId.of("b5ce7d09-2c51-4aa5-bae4-4c567149dd32"),
                Price.of("8.20", PLN),
                Weight.of(0.15, KG),
                Quantity.of(3)
            ), Price.of("35.00", USD)));

        // when delivery cost is calculated
        final var deliveryCost = calculator.calculate(orderItems);

        // then delivery cost is 15 USD
        assertThat(deliveryCost).isEqualTo(Price.of("15.00", USD));
    }

    @Test
    void should_calculate_12_usd_delivery_cost() {
        // given order items
        final var orderItems = new ExchangedOrderItemList(USD,
            new ExchangedOrderItem(new OrderItem(
                ProductId.of("b5ce7d09-2c51-4aa5-bae4-4c567149dd32"),
                Price.of("16.20", PLN),
                Weight.of(0.15, KG),
                Quantity.of(3)
            ), Price.of("70.30", USD)));

        // when delivery cost is calculated
        final var deliveryCost = calculator.calculate(orderItems);

        // then delivery cost is 15 USD
        assertThat(deliveryCost).isEqualTo(Price.of("12.00", USD));
    }

    @Test
    void should_calculate_free_delivery_cost() {
        // given order items
        final var orderItems = new ExchangedOrderItemList(USD,
            new ExchangedOrderItem(new OrderItem(
                ProductId.of("b5ce7d09-2c51-4aa5-bae4-4c567149dd32"),
                Price.of("33.20", PLN),
                Weight.of(0.6, KG),
                Quantity.of(3)
            ), Price.of("150.99", USD)));

        // when delivery cost is calculated
        final var deliveryCost = calculator.calculate(orderItems);

        // then delivery cost is 15 USD
        assertThat(deliveryCost).isEqualTo(Price.of("00.00", USD));
    }

    @Test
    void should_calculate_default_delivery_cost() {
        // given order items
        final var orderItems = new ExchangedOrderItemList(USD,
            new ExchangedOrderItem(new OrderItem(
                ProductId.of("b5ce7d09-2c51-4aa5-bae4-4c567149dd32"),
                Price.of("33.20", PLN),
                Weight.of(0.9, KG),
                Quantity.of(3)
            ), Price.of("150.99", USD)));

        // when delivery cost is calculated
        final var deliveryCost = calculator.calculate(orderItems);

        // then delivery cost is 15 USD
        assertThat(deliveryCost).isEqualTo(Price.of("20.00", USD));
    }
}