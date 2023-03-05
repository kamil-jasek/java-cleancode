package pl.sda.refactoring.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pl.sda.refactoring.entity.*;
import pl.sda.refactoring.service.OrderSettings.DiscountSettings;
import pl.sda.refactoring.service.command.MakeOrder;
import pl.sda.refactoring.service.port.CurrencyExchangerPort;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private final CurrencyExchangerPort currencyExchanger = new TestCurrencyExchanger();
    private final OrderRepo orderRepo = mock(OrderRepo.class);
    private final EmailService emailService = mock(EmailService.class);
    private final Clock fixedClock = Clock.fixed(Instant.parse("2022-03-04T10:00:00Z"), UTC);
    private final OrderSettings orderSettings = new OrderSettings(
        new DiscountSettings(List.of(LocalDate.now(fixedClock))),
        List.of("test@test.pl"));

    private final OrderService orderService = new OrderService(
        new DeliveryPriceCalculator(currencyExchanger),
        new CustomerValidator(new TestCustomerDatabase()),
        orderRepo,
        emailService,
        orderSettings,
        new OrderItemCurrencyExchanger(currencyExchanger),
        new DiscountPriceCalculator(
            new TestDiscountService(),
            orderSettings,
            fixedClock),
        fixedClock);

    @Test
    void should_make_order() {
        // given
        UUID customerId = UUID.fromString("f363c254-6bc3-440b-b120-4870999da0d9");

        // when make order is executed
        UUID id = orderService.handle(
            new MakeOrder(customerId, List.of(OrderItem.builder()
                .productId(UUID.fromString("50df82ab-f553-4c53-83c2-0bf993ffaab9"))
                .price(new BigDecimal("12.00"))
                .currency(Currency.PLN)
                .weight(0.2)
                .weightUnit(WeightUnit.KG)
                .quantity(2)
                .build()), Currency.USD, "ABC20"));

        // then
        assertThat(id).isNotNull();
        var orderCapture = ArgumentCaptor.forClass(Order.class);
        verify(orderRepo).save(orderCapture.capture());
        var order = orderCapture.getValue();
        assertThat(order).usingRecursiveComparison()
            .ignoringFields("items.id")
            .isEqualTo(Order.builder()
                .id(order.id())
                .customerId(customerId)
                .status(OrderStatus.CONFIRMED)
                .currency(Currency.USD)
                .ctime(fixedClock.instant())
                .items(List.of(
                    OrderItem.builder()
                        .productId(UUID.fromString("50df82ab-f553-4c53-83c2-0bf993ffaab9"))
                        .price(new BigDecimal("12.00"))
                        .currency(Currency.PLN)
                        .weight(0.2)
                        .weightUnit(WeightUnit.KG)
                        .exchPrice(new BigDecimal("52.00"))
                        .quantity(2)
                        .build()
                ))
                .discount(new BigDecimal("25.40"))
                .delivery(new BigDecimal("15.00"))
                .totalExch(new BigDecimal("104.00"))
                .discountedTotal(new BigDecimal("93.60"))
                .build());
        verify(emailService).send(any(), any(), any());
    }

    @Test
    void should_fail_if_empty_order_item_list() {
        // expect error
        assertThrows(EmptyOrderItemsListException.class, () ->
            new MakeOrder(UUID.randomUUID(),
                emptyList(),
                Currency.USD,
                "ABC20"));
    }
}
