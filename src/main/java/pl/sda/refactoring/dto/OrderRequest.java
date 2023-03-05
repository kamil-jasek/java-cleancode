package pl.sda.refactoring.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.UUID;
import pl.sda.refactoring.service.domain.Currency;
import pl.sda.refactoring.entity.OrderItemEntity;
import pl.sda.refactoring.service.domain.WeightUnit;
import pl.sda.refactoring.service.command.MakeOrder;

import java.util.List;

import static java.util.stream.Collectors.toList;

public record OrderRequest(
    @NotNull @UUID String customerId,
    @NotNull @Pattern(regexp = "USD|EUR|PLN") String orderCurrency,
    @NotNull @NotEmpty @Valid List<OrderItemDto> orderItems,
    String discountCoupon
) {
    public MakeOrder toCommand() {
        return new MakeOrder(
            java.util.UUID.fromString(customerId()),
            orderItems().stream()
                .map(item -> OrderItemEntity.builder()
                    .productId(java.util.UUID.fromString(item.productId()))
                    .price(item.price())
                    .currency(Currency.valueOf(item.currency()))
                    .weight(item.weight().doubleValue())
                    .weightUnit(WeightUnit.valueOf(item.weightUnit()))
                    .quantity(item.quantity())
                    .build())
                .collect(toList()),
            Currency.valueOf(orderCurrency()),
            discountCoupon());
    }
}
