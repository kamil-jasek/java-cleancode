package pl.sda.refactoring.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import pl.sda.refactoring.service.OrderSettings.DiscountSettings;
import pl.sda.refactoring.service.command.MakeOrder;
import pl.sda.refactoring.service.domain.*;
import pl.sda.refactoring.service.port.CurrencyExchangerPort;
import pl.sda.refactoring.service.port.OrderRepoPort;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.time.ZoneOffset.UTC;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static pl.sda.refactoring.service.domain.Currency.PLN;
import static pl.sda.refactoring.service.domain.Currency.USD;
import static pl.sda.refactoring.service.domain.OrderStatus.CONFIRMED;
import static pl.sda.refactoring.service.domain.WeightUnit.KG;

class OrderServiceTest {

    private final CurrencyExchangerPort currencyExchanger = new TestCurrencyExchanger();
    private final OrderRepoPort orderRepoPort = mock(OrderRepoPort.class);
    private final EmailService emailService = mock(EmailService.class);
    private final Clock fixedClock = Clock.fixed(Instant.parse("2022-03-04T10:00:00Z"), UTC);
    private final OrderSettings orderSettings = new OrderSettings(
        new DiscountSettings(List.of(LocalDate.now(fixedClock))),
        List.of("test@test.pl"));

    private final OrderService orderService = new OrderService(
        new DeliveryPriceCalculator(currencyExchanger),
        new CustomerValidator(new TestCustomerDatabase()),
        orderRepoPort,
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
        final var customerId = CustomerId.of("f363c254-6bc3-440b-b120-4870999da0d9");

        // when make order is executed
        final var orderId = orderService.handle(new MakeOrder(
            customerId,
            List.of(new OrderItem(
                new ProductId(UUID.fromString("8af9eb6e-7366-4133-8e62-f2649de99589")),
                Price.of("12.00", PLN),
                new Weight(0.2, KG),
                new Quantity(2)
            )),
            USD,
            "ABC20"));

        // then
        assertThat(orderId).isNotNull();
        var orderCapture = ArgumentCaptor.forClass(Order.class);
        verify(orderRepoPort).save(orderCapture.capture());
        var order = orderCapture.getValue();
        assertThat(order).isEqualTo(new Order(
            orderId,
            new CreateTime(fixedClock.instant()),
            CONFIRMED,
            customerId,
            new ExchangedOrderItemList(List.of(
                new ExchangedOrderItem(
                    new OrderItem(
                        new ProductId(UUID.fromString("8af9eb6e-7366-4133-8e62-f2649de99589")),
                        Price.of("12.00", PLN),
                        new Weight(0.2, KG),
                        new Quantity(2)
                    ),
                    Price.of("52.00", USD)
                )
            ), USD),
            Price.of("25.40", USD),
            Price.of("15.00", USD)
        ));
//        verify(emailService).send(any(), any(), any());
    }

    @Test
    void should_fail_if_empty_order_item_list() {
        // expect error
        assertThrows(EmptyOrderItemsListException.class, () ->
            new MakeOrder(new CustomerId(UUID.randomUUID()),
                emptyList(),
                USD,
                "ABC20"));
    }
}
