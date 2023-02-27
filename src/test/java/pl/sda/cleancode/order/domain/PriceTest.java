package pl.sda.cleancode.order.domain;

import org.junit.jupiter.api.Test;
import pl.sda.cleancode.application.exception.DomainIllegalArgumentException;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static pl.sda.cleancode.order.domain.Currency.PLN;
import static pl.sda.cleancode.order.domain.Currency.USD;

class PriceTest {

    @Test
    void cannot_be_negative() {
        assertDoesNotThrow(() -> new Price(new BigDecimal("10.00"), PLN));
        assertThrows(DomainIllegalArgumentException.class, () -> new Price(new BigDecimal("-0.01"), PLN));
    }

    @Test
    void can_be_added_if_the_same_currency() {
        // given two prices in the same currency
        final var priceOne = Price.of("1.00", USD);
        final var priceTwo = Price.of("2.00", USD);

        // when prices are added
        final var resultPrice = priceOne.plus(priceTwo);

        // then expect 3.00 USD
        assertThat(resultPrice).isEqualTo(Price.of("3.00", USD));
    }

    @Test
    void cannot_be_added_when_different_currencies() {
        // given two prices with different currencies
        final var priceOne = Price.of("1.00", PLN);
        final var priceTwo = Price.of("2.00", USD);

        // expect exception
        assertThrows(DomainIllegalArgumentException.class, () -> priceOne.plus(priceTwo));
    }
}