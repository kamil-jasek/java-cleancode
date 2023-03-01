package pl.sda.refactoring.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.UUID;

import java.util.List;

public record OrderDto(
    @NotNull @UUID String customerId,
    @NotNull @Pattern(regexp = "USD|EUR|PLN") String orderCurrency,
    @NotNull @NotEmpty List<OrderItemDto> orderItems,
    String discountCoupon
) {
}
