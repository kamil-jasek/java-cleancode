package pl.sda.refactoring.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sda.refactoring.service.command.MakeOrder;
import pl.sda.refactoring.service.domain.CreateTime;
import pl.sda.refactoring.service.domain.Order;
import pl.sda.refactoring.service.domain.OrderId;
import pl.sda.refactoring.service.domain.OrderStatus;
import pl.sda.refactoring.service.port.OrderRepoPort;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private final OrderRepoPort orderRepoPort;
    private final EmailService emailService;
    private final List<String> emailRecipients;
    private final CustomerValidator customerValidator;
    private final DiscountPriceCalculator discountPriceCalculator;
    private final DeliveryPriceCalculator deliveryPriceCalculator;
    private final OrderItemCurrencyExchanger orderItemCurrencyExchanger;
    private final Clock clock;

    public OrderService(@NonNull DeliveryPriceCalculator deliveryPriceCalculator,
                        @NonNull CustomerValidator customerValidator,
                        @NonNull OrderRepoPort orderRepoPort,
                        @NonNull EmailService emailService,
                        @NonNull OrderSettings settings,
                        @NonNull OrderItemCurrencyExchanger currencyExchanger,
                        @NonNull DiscountPriceCalculator discountPriceCalculator,
                        @NonNull Clock clock) {
        this.deliveryPriceCalculator = deliveryPriceCalculator;
        this.customerValidator = customerValidator;
        this.orderRepoPort = orderRepoPort;
        this.emailService = emailService;
        this.emailRecipients = settings.emailRecipients();
        this.orderItemCurrencyExchanger = currencyExchanger;
        this.discountPriceCalculator = discountPriceCalculator;
        this.clock = clock;
    }

    public OrderId handle(MakeOrder cmd) {
        customerValidator.assertCustomerExists(cmd.customerId().value());
        final var exchangedItems = orderItemCurrencyExchanger.exchangeCurrencies(
            cmd.orderItems(),
            cmd.baseCurrency());
        final var deliveryPrice = deliveryPriceCalculator.calculateDeliveryPrice(exchangedItems);
        final var discountPrice = discountPriceCalculator.calculateDiscountPrice(
            new DiscountContext(cmd.customerId(),
                exchangedItems.totalPrice(),
                deliveryPrice,
                cmd.coupon()));
        final var order = new Order(
            new OrderId(UUID.randomUUID()),
            new CreateTime(Instant.now(clock)),
            OrderStatus.CONFIRMED,
            cmd.customerId(),
            exchangedItems,
            discountPrice,
            deliveryPrice);
        orderRepoPort.save(order);
//        sendEmail(order);
        return order.orderId();
    }

//    private void sendEmail(OrderEntity order) {
//        emailService.send(
//            "New order confirmed: " + order.id(),
//            """
//                New order has been confirmed:
//                id: %s
//                create time: %s
//                currency: %s
//                customer id: %s
//                total price: %s
//                discount: %s
//                discounted price: %s
//                delivery cost: %s
//                """.formatted(
//                order.id(),
//                order.ctime(),
//                order.currency(),
//                order.customerId(),
//                order.totalExch(),
//                order.discount(),
//                order.discountedTotal(),
//                order.delivery()),
//            emailRecipients);
//    }
}
