package pl.sda.refactoring.service.port;

import pl.sda.refactoring.entity.Currency;

import java.math.BigDecimal;

public interface CurrencyExchangerPort {
    BigDecimal exchange(BigDecimal price, Currency original, Currency target);
}
