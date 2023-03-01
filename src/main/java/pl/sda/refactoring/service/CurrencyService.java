package pl.sda.refactoring.service;

import org.springframework.stereotype.Service;
import pl.sda.refactoring.entity.Currency;

import java.math.BigDecimal;

@Service
public class CurrencyService {
    public BigDecimal exchange(BigDecimal price, Currency original, Currency target) {
        throw new InfrastructureException("currency service failure");
    }
}
