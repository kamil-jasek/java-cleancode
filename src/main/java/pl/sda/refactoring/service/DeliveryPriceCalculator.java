package pl.sda.refactoring.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sda.refactoring.entity.Currency;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
final class DeliveryPriceCalculator {

    private final CurrencyService currencyService;

    BigDecimal calculateDeliveryPrice(@NonNull BigDecimal totalPrice,
                                      int totalWeightInGrams,
                                      @NonNull Currency baseCurrency) {
        var deliveryPrice = BigDecimal.ZERO;
        // delivery costs are in USD
        var tpInUsd = currencyService.exchange(totalPrice, baseCurrency, Currency.USD);
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
        deliveryPrice = currencyService.exchange(deliveryPrice, Currency.USD, baseCurrency);
        return deliveryPrice;
    }
}