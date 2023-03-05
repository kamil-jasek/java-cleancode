package pl.sda.refactoring.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.UUID;
import pl.sda.refactoring.service.command.MakeOrder;
import pl.sda.refactoring.service.domain.*;

import java.util.List;

import static java.util.UUID.fromString;
import static java.util.stream.Collectors.toList;

public record OrderRequest(
    @NotNull @UUID String customerId,
    @NotNull @Pattern(regexp = "USD|EUR|PLN") String orderCurrency,
    @NotNull @NotEmpty @Valid List<OrderItemDto> orderItems,
    String discountCoupon
) {
    public MakeOrder toCommand() {
        return new MakeOrder(
            CustomerId.of(customerId),
            orderItems().stream()
                .map(item -> new OrderItem(
                    new ProductId(fromString(item.productId())),
                    new Price(item.price(), Currency.valueOf(item.currency())),
                    new Weight(item.weight().doubleValue(), WeightUnit.valueOf(item.weightUnit())),
                    new Quantity(item.quantity())
                ))
                .collect(toList()),
            Currency.valueOf(orderCurrency),
            discountCoupon);
    }
}
