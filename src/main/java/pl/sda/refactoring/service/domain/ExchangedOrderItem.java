package pl.sda.refactoring.service.domain;

import lombok.NonNull;

import java.math.BigDecimal;

public record ExchangedOrderItem(@NonNull OrderItem item, @NonNull Price exchangedPrice) {

    public Price fullExchangedPrice() {
        return exchangedPrice().multiply(BigDecimal.valueOf(item().quantity().value()));
    }
}
