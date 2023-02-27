package pl.sda.cleancode.order.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.sda.cleancode.order.domain.Currency;
import pl.sda.cleancode.order.domain.ExchangedOrderItem;
import pl.sda.cleancode.order.domain.ExchangedOrderItemList;
import pl.sda.cleancode.order.domain.OrderItem;
import pl.sda.cleancode.order.infra.port.ExchangeCurrencyPort;

import java.util.List;

@RequiredArgsConstructor
final class OrderItemCurrencyExchanger {

    private final @NonNull ExchangeCurrencyPort currencyPort;

    ExchangedOrderItemList exchangeCurrencies(List<OrderItem> orderItems,
                                              Currency orderCurrency) {
        return new ExchangedOrderItemList(orderCurrency, orderItems
            .stream()
            .map(orderItem -> exchangeOrderItem(orderCurrency, orderItem))
            .toArray(ExchangedOrderItem[]::new));
    }

    private ExchangedOrderItem exchangeOrderItem(Currency currency, OrderItem orderItem) {
        return new ExchangedOrderItem(orderItem, currencyPort.exchange(orderItem.price(), currency));
    }
}
