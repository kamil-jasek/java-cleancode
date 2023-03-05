package pl.sda.refactoring.service;

import pl.sda.refactoring.entity.Currency;
import pl.sda.refactoring.service.port.CurrencyExchangerPort;

import java.math.BigDecimal;
import java.util.Map;

import static java.math.RoundingMode.HALF_UP;
import static pl.sda.refactoring.entity.Currency.PLN;
import static pl.sda.refactoring.entity.Currency.USD;

final class TestCurrencyExchanger implements CurrencyExchangerPort {

    private final Map<Currency, Map<Currency, BigDecimal>> exchanges = Map.of(
        PLN, Map.of(
            USD, new BigDecimal("4.3333")
        ),
        USD, Map.of(
            USD, new BigDecimal("1.00")
        ));

    @Override
    public BigDecimal exchange(BigDecimal price, Currency original, Currency target) {
        return price.multiply(exchanges.get(original).get(target)).setScale(2, HALF_UP);
    }
}
