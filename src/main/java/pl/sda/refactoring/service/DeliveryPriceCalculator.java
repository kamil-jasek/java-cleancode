package pl.sda.refactoring.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sda.refactoring.service.domain.*;
import pl.sda.refactoring.service.port.CurrencyExchangerPort;

import java.math.BigDecimal;

import static pl.sda.refactoring.service.domain.Currency.USD;
import static pl.sda.refactoring.service.domain.WeightUnit.KG;

@Component
@RequiredArgsConstructor
final class DeliveryPriceCalculator {

    private final CurrencyExchangerPort currencyExchanger;

    Price calculateDeliveryPrice(@NonNull ExchangedOrderItemList itemList) {
        return currencyExchanger.exchange(getPrice(itemList), itemList.baseCurrency());
    }

    private Price getPrice(ExchangedOrderItemList itemList) {
        var totalPrice = currencyExchanger.exchange(itemList.totalPrice(), USD);
        if (totalPrice.gt(Price.of("400.00", USD)) &&
            itemList.totalWeight().lt(new Weight(2, KG))) {
            return Price.of("0.00", USD); // free delivery
        } else if (totalPrice.gt(Price.of("200.00", USD)) &&
            itemList.totalWeight().lt(new Weight(1, KG))) {
            return Price.of("12.00", USD); // first level
        } else if (totalPrice.gt(Price.of("100.00", USD)) &&
            itemList.totalWeight().lt(new Weight(1, KG))) {
            return Price.of("15.00", USD); // second level
        } else {
            // default delivery
            return Price.of("15.00", USD);
        }
    }
}