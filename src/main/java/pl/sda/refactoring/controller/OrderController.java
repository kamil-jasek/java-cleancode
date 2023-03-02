package pl.sda.refactoring.controller;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.sda.refactoring.dto.OrderDto;
import pl.sda.refactoring.entity.Currency;
import pl.sda.refactoring.entity.OrderItem;
import pl.sda.refactoring.entity.WeightUnit;
import pl.sda.refactoring.service.OrderService;

import java.net.URI;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final @NonNull OrderService orderService;

    @PostMapping
    @SneakyThrows
    ResponseEntity<Void> makeOrder(@RequestBody @Valid OrderDto orderDto) {
        var orderId = orderService.makeOrder(
            UUID.fromString(orderDto.customerId()),
            orderDto.orderItems().stream()
                .map(item -> OrderItem.builder()
                    .productId(UUID.fromString(item.productId()))
                    .price(item.price())
                    .currency(Currency.valueOf(item.currency()))
                    .weight(item.weight().doubleValue())
                    .weightUnit(WeightUnit.valueOf(item.weightUnit()))
                    .quantity(item.quantity())
                    .build())
                .collect(toList()),
            Currency.valueOf(orderDto.orderCurrency()),
            orderDto.discountCoupon()
        );
        return ResponseEntity.created(new URI("/api/orders/" + orderId)).build();
    }
}
