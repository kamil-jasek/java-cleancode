package pl.sda.refactoring.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sda.refactoring.service.domain.Currency;
import pl.sda.refactoring.service.domain.ExchangedOrderItem;
import pl.sda.refactoring.service.domain.ExchangedOrderItemList;
import pl.sda.refactoring.service.domain.OrderItem;
import pl.sda.refactoring.service.port.CurrencyExchangerPort;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
final class OrderItemCurrencyExchanger {

    private final CurrencyExchangerPort currencyExchanger;

    ExchangedOrderItemList exchangeCurrencies(@NonNull List<OrderItem> orderItems,
                                              @NonNull Currency baseCurrency) {
        return new ExchangedOrderItemList(orderItems
            .stream()
            .map(item -> new ExchangedOrderItem(
                item,
                currencyExchanger.exchange(item.price(), baseCurrency)))
            .collect(Collectors.toList()),
            baseCurrency);
    }
}