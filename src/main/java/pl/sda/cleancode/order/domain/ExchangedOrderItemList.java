package pl.sda.cleancode.order.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Arrays;
import java.util.List;

import static java.math.BigDecimal.ZERO;
import static pl.sda.cleancode.application.util.DomainArgumentCheck.check;

@Getter
@EqualsAndHashCode
@ToString
public final class ExchangedOrderItemList {
    private final Currency baseCurrency;
    private final List<ExchangedOrderItem> orderItems;
    private final Price totalPrice;
    private final Weight totalWeight;

    public ExchangedOrderItemList(@NonNull Currency baseCurrency,
                                  @NonNull ExchangedOrderItem... orderItems) {
        this.baseCurrency = baseCurrency;
        check(orderItems.length > 0, "at least one order item is required");
        this.orderItems = Arrays
            .stream(orderItems)
            .toList();
        check(allExchangePricesMatchesBaseCurrency(),
            "all order items must match orderCurrency: " + baseCurrency);
        totalPrice = this.orderItems
            .stream()
            .map(ExchangedOrderItem::fullPrice)
            .reduce(new Price(ZERO, baseCurrency), Price::plus);
        totalWeight = this.orderItems
            .stream()
            .map(ExchangedOrderItem::fullWeight)
            .reduce(Weight.of(0, WeightUnit.GM), Weight::plus);
    }

    private boolean allExchangePricesMatchesBaseCurrency() {
        return orderItems
            .stream()
            .allMatch(exchangedOrderItem -> exchangedOrderItem.hasExchangedCurrency(baseCurrency));
    }

    public Price totalPrice() {
        return totalPrice;
    }

    public Weight totalWeight() {
        return totalWeight;
    }
}
