package pl.sda.refactoring.service.command;

import lombok.NonNull;
import pl.sda.refactoring.entity.Currency;
import pl.sda.refactoring.entity.OrderItem;
import pl.sda.refactoring.service.EmptyOrderItemsListException;

import java.util.List;
import java.util.UUID;

public record MakeOrder(
    @NonNull UUID customerId,
    @NonNull List<OrderItem> orderItems,
    @NonNull Currency baseCurrency,
    String coupon
) {

    public MakeOrder {
        if (orderItems.isEmpty()) {
            throw new EmptyOrderItemsListException();
        }
    }
}