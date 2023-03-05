package pl.sda.refactoring.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sda.refactoring.service.domain.Currency;
import pl.sda.refactoring.service.port.CurrencyExchangerPort;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
final class DeliveryPriceCalculator {

    private final CurrencyExchangerPort currencyExchanger;

    BigDecimal calculateDeliveryPrice(@NonNull BigDecimal totalPrice,
                                      int totalWeightInGrams,
                                      @NonNull Currency baseCurrency) {
        var deliveryPrice = BigDecimal.ZERO;
        // delivery costs are in USD
        var tpInUsd = currencyExchanger.exchange(totalPrice, baseCurrency, Currency.USD);
        if (tpInUsd.compareTo(new BigDecimal("400.00")) > 0 && totalWeightInGrams < 2000) {
            deliveryPrice = new BigDecimal("0.00"); // free delivery
        } else if (tpInUsd.compareTo(new BigDecimal("200.00")) > 0 && totalWeightInGrams < 1000) {
            deliveryPrice = new BigDecimal("12.00"); // first level
        } else if (tpInUsd.compareTo(new BigDecimal("100.00")) > 0 && totalWeightInGrams < 1000) {
            deliveryPrice = new BigDecimal("15.00"); // second level
        } else {
            // default delivery
            deliveryPrice = new BigDecimal("20.00");
        }
        deliveryPrice = currencyExchanger.exchange(deliveryPrice, Currency.USD, baseCurrency);
        return deliveryPrice;
    }
}