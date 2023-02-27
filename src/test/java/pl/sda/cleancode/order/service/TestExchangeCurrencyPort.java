package pl.sda.cleancode.order.service;

import pl.sda.cleancode.order.domain.Currency;
import pl.sda.cleancode.order.domain.Price;
import pl.sda.cleancode.order.infra.port.ExchangeCurrencyPort;

import java.math.BigDecimal;
import java.util.Map;

import static pl.sda.cleancode.order.domain.Currency.*;

final class TestExchangeCurrencyPort implements ExchangeCurrencyPort {

    private final Map<Currency, Map<Currency, Double>> exchangeRates = Map.of(
        USD, Map.of(
            PLN, 4.4145,
            EUR, 1.0342
        )
    );

    @Override
    public Price exchange(Price price, Currency currency) {
        return price.exchangeTo(currency, getExchangeRate(price, currency));
    }

    private BigDecimal getExchangeRate(Price price, Currency currency) {
        return price.currency().equals(currency)
            ? BigDecimal.ONE
            : BigDecimal.valueOf(exchangeRates
                .get(currency)
                .get(price.currency()));
    }
}
