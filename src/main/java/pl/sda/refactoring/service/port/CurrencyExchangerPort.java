package pl.sda.refactoring.service.port;

import pl.sda.refactoring.service.domain.Currency;

import java.math.BigDecimal;

public interface CurrencyExchangerPort {
    BigDecimal exchange(BigDecimal price, Currency original, Currency target);
}
