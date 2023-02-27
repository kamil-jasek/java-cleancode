package pl.sda.cleancode.order.infra.port;

import pl.sda.cleancode.order.domain.Currency;
import pl.sda.cleancode.order.domain.Price;

public interface ExchangeCurrencyPort {
    Price exchange(Price price, Currency currency);
}
