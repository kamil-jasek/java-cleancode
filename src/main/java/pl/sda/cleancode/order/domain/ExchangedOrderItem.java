package pl.sda.cleancode.order.domain;

import java.math.BigDecimal;

public record ExchangedOrderItem(OrderItem orderItem, Price exchangedPrice) {

    public boolean hasExchangedCurrency(Currency currency) {
        return exchangedPrice.currency().equals(currency);
    }

    public Price fullPrice() {
        return exchangedPrice.multiply(BigDecimal.valueOf(orderItem.quantity().value()));
    }

    public Weight fullWeight() {
        return orderItem.weight().multiply(orderItem.quantity().value());
    }
}
