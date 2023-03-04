package pl.sda.refactoring.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sda.refactoring.entity.Order;
import pl.sda.refactoring.entity.OrderItem;
import pl.sda.refactoring.entity.OrderStatus;
import pl.sda.refactoring.service.command.MakeOrder;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.List;
import java.util.UUID;

import static java.math.RoundingMode.HALF_UP;
import static java.util.UUID.randomUUID;

@Service
@Transactional
public class OrderService {

    private final OrderRepo orderRepo;
    private final EmailService emailService;
    private final List<String> emailRecipients;
    private final CustomerValidator customerValidator;
    private final DiscountPriceCalculator discountPriceCalculator;
    private final DeliveryPriceCalculator deliveryPriceCalculator;
    private final OrderItemCurrencyExchanger orderItemCurrencyExchanger;
    private final Clock clock;

    public OrderService(@NonNull DeliveryPriceCalculator deliveryPriceCalculator,
                        @NonNull CustomerValidator customerValidator,
                        @NonNull OrderRepo orderRepo,
                        @NonNull EmailService emailService,
                        @NonNull OrderSettings settings,
                        @NonNull OrderItemCurrencyExchanger currencyExchanger,
                        @NonNull DiscountPriceCalculator discountPriceCalculator,
                        @NonNull Clock clock) {
        this.deliveryPriceCalculator = deliveryPriceCalculator;
        this.customerValidator = customerValidator;
        this.orderRepo = orderRepo;
        this.emailService = emailService;
        this.emailRecipients = settings.emailRecipients();
        this.orderItemCurrencyExchanger = currencyExchanger;
        this.discountPriceCalculator = discountPriceCalculator;
        this.clock = clock;
    }

    public UUID handle(MakeOrder cmd) {
        customerValidator.assertCustomerExists(cmd.customerId());
        final var exchangedItems = orderItemCurrencyExchanger.exchangeCurrenciesInItems(
            cmd.orderItems(),
            cmd.baseCurrency());
        final var totalPrice = totalPrice(exchangedItems);
        final var totalWeightInGrams = totalWeightInGrams(exchangedItems);
        final var deliveryPrice = deliveryPriceCalculator.calculateDeliveryPrice(
            totalPrice,
            totalWeightInGrams,
            cmd.baseCurrency());
        final var discountPrice = discountPriceCalculator.calculateDiscountPrice(
            totalPrice,
            deliveryPrice,
            cmd.coupon(),
            cmd.customerId());
        final var order = Order.builder()
            .id(randomUUID())
            .ctime(clock.instant())
            .currency(cmd.baseCurrency())
            .status(OrderStatus.CONFIRMED)
            .customerId(cmd.customerId())
            .items(exchangedItems)
            .totalExch(totalPrice)
            .delivery(deliveryPrice)
            .discount(discountPrice)
            .discountedTotal(totalPrice.add(deliveryPrice).subtract(discountPrice))
            .build();
        orderRepo.save(order);
        sendEmail(order);
        return order.id();
    }

    private static int totalWeightInGrams(List<OrderItem> exchangedItems) {
        int twInGrams = 0;
        for (var item : exchangedItems) {
            switch (item.weightUnit()) {
                case GM -> twInGrams += item.weight();
                case KG -> twInGrams += item.weight() * 1000;
                case LB -> twInGrams += item.weight() * 453.59237;
            }
        }
        return twInGrams;
    }

    private static BigDecimal totalPrice(List<OrderItem> exchangedItems) {
        BigDecimal tp = BigDecimal.ZERO;
        for (var item : exchangedItems) {
            tp = tp.add(item
                    .exchPrice()
                    .multiply(BigDecimal.valueOf(item.quantity())))
                .setScale(2, HALF_UP);
        }
        return tp;
    }

    private void sendEmail(Order order) {
        emailService.send(
            "New order confirmed: " + order.id(),
            """
                New order has been confirmed:
                id: %s
                create time: %s
                currency: %s
                customer id: %s
                total price: %s
                discount: %s
                discounted price: %s
                delivery cost: %s
                """.formatted(
                order.id(),
                order.ctime(),
                order.currency(),
                order.customerId(),
                order.totalExch(),
                order.discount(),
                order.discountedTotal(),
                order.delivery()),
            emailRecipients);
    }
}
