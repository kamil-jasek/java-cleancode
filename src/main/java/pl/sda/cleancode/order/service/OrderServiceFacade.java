package pl.sda.cleancode.order.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import pl.sda.cleancode.application.event.DomainEventPublisher;
import pl.sda.cleancode.order.command.MakeOrderCmd;
import pl.sda.cleancode.order.domain.Order;
import pl.sda.cleancode.order.domain.OrderConfirmation;
import pl.sda.cleancode.order.domain.OrderId;
import pl.sda.cleancode.order.event.OrderMadeEvent;
import pl.sda.cleancode.order.event.OrderMadeEventData;
import pl.sda.cleancode.order.exception.CustomerNotExistsException;
import pl.sda.cleancode.order.infra.port.CustomerPort;
import pl.sda.cleancode.order.infra.port.OrderRepoPort;
import pl.sda.cleancode.order.service.DiscountCalculator.DiscountContext;

import java.time.Clock;
import java.time.Instant;

import static java.util.UUID.randomUUID;

@Transactional
@RequiredArgsConstructor
public class OrderServiceFacade {

    private final @NonNull CustomerPort customerPort;
    private final @NonNull OrderRepoPort orderRepoPort;
    private final @NonNull Clock clock;
    private final @NonNull OrderItemCurrencyExchanger orderItemCurrencyExchanger;
    private final @NonNull DeliveryCostCalculator deliveryCostCalculator;
    private final @NonNull DiscountCalculator discountCalculator;
    private final @NonNull DomainEventPublisher eventPublisher;

    public OrderConfirmation handle(MakeOrderCmd cmd) {
        if (!customerPort.customerExists(cmd.customerId())) {
            throw new CustomerNotExistsException(cmd.customerId());
        }
        final var exchangedOrderItems = orderItemCurrencyExchanger.exchangeCurrencies(
            cmd.orderItemList(),
            cmd.orderCurrency());
        final var deliveryCost = deliveryCostCalculator.calculate(exchangedOrderItems);
        final var discount = discountCalculator.calculate(new DiscountContext(
                cmd.customerId(),
                exchangedOrderItems,
                deliveryCost,
                cmd.discountCoupon()))
            .discount();
        final var order = new Order(
            OrderId.generate(),
            clock.instant(),
            cmd.customerId(),
            exchangedOrderItems,
            deliveryCost,
            discount);
        orderRepoPort.save(order);
        eventPublisher.publish(new OrderMadeEvent(
            randomUUID(),
            Instant.now(clock),
            null,
            OrderMadeEventData.from(order)));
        return new OrderConfirmation(order.id());
    }
}
