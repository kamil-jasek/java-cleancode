package pl.sda.refactoring.service.port.adapter.currency;

import org.springframework.stereotype.Service;
import pl.sda.refactoring.service.domain.Currency;
import pl.sda.refactoring.service.InfrastructureException;
import pl.sda.refactoring.service.port.CurrencyExchangerPort;

import java.math.BigDecimal;

@Service
final class NbpCurrencyExchanger implements CurrencyExchangerPort {
    @Override
    public BigDecimal exchange(BigDecimal price, Currency original, Currency target) {
        throw new InfrastructureException("baseCurrency service failure");
    }
}
