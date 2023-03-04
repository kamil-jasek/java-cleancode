package pl.sda.refactoring.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sda.refactoring.entity.Currency;
import pl.sda.refactoring.entity.Order;
import pl.sda.refactoring.entity.OrderItem;
import pl.sda.refactoring.entity.OrderStatus;
import pl.sda.refactoring.service.command.MakeOrder;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.math.RoundingMode.HALF_UP;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

@Service
@Transactional
public class OrderService {

    private final DiscountService discountService;
    private final CurrencyService currencyService;
    private final OrderRepo orderRepo;
    private final EmailService emailService;
    private final List<LocalDate> freeDeliveryDays;
    private final List<String> emailRecipients;
    private final CustomerValidator customerValidator;

    public OrderService(@NonNull DiscountService discountService,
                        @NonNull CurrencyService currencyService,
                        @NonNull CustomerValidator customerValidator,
                        @NonNull OrderRepo orderRepo,
                        @NonNull EmailService emailService,
                        @NonNull OrderSettings settings) {
        this.discountService = discountService;
        this.currencyService = currencyService;
        this.customerValidator = customerValidator;
        this.orderRepo = orderRepo;
        this.emailService = emailService;
        this.freeDeliveryDays = settings.discountSettings().freeDeliveryDays();
        this.emailRecipients = settings.emailRecipients();
    }

    public UUID handle(MakeOrder cmd) {
        customerValidator.assertCustomerExists(cmd);
        final var exchangedItems = exchangeCurrenciesInItems(cmd.orderItems(), cmd.baseCurrency());
        final var totalPrice = totalPrice(exchangedItems);
        final var totalWeightInGrams = totalWeightInGrams(exchangedItems);
        final var deliveryPrice = calculateDeliveryPrice(totalPrice, totalWeightInGrams, cmd.baseCurrency());
        final var discountPrice = calculateDiscountPrice(totalPrice,
            deliveryPrice,
            cmd.coupon(),
            cmd.customerId());
        final var order = Order.builder()
            .id(randomUUID())
            .ctime(Instant.now())
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

    private BigDecimal calculateDiscountPrice(BigDecimal totalPrice,
                                              BigDecimal deliveryPrice,
                                              String coupon,
                                              @NonNull UUID customerId) {
        var discountPrice = BigDecimal.ZERO;
        if (coupon != null) {
            var discount = discountService.getDiscount(coupon);
            if (discount != null) {
                discountPrice = totalPrice
                    .multiply(BigDecimal.valueOf(discount.value()))
                    .setScale(2, HALF_UP);
                discountService.deactivate(coupon, customerId);
            }
        }
        if (freeDeliveryDays.contains(LocalDate.now())) {
            discountPrice = discountPrice.add(deliveryPrice);
        }
        return discountPrice;
    }

    private BigDecimal calculateDeliveryPrice(@NonNull BigDecimal totalPrice,
                                              int totalWeightInGrams,
                                              @NonNull Currency baseCurrency) {
        var deliveryPrice = BigDecimal.ZERO;
        // delivery costs are in USD
        var tpInUsd = currencyService.exchange(totalPrice, baseCurrency, Currency.USD);
        if (tpInUsd.compareTo(new BigDecimal("400.00")) > 0 && totalWeightInGrams < 2000) {
            deliveryPrice = new BigDecimal("0.00"); // free delivery
        } else if (tpInUsd.compareTo(new BigDecimal("200.00")) > 0 && totalWeightInGrams < 1000) {
            deliveryPrice = new BigDecimal("12.00"); // first level
        } else if (tpInUsd.compareTo(new BigDecimal("100.00")) > 0 && totalWeightInGrams < 1000) {
            deliveryPrice = new BigDecimal("15.00"); // second level
        } else {
            // default delivery
            deliveryPrice = new BigDecimal("20.00");
        }
        deliveryPrice = currencyService.exchange(deliveryPrice, Currency.USD, baseCurrency);
        return deliveryPrice;
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

    private List<OrderItem> exchangeCurrenciesInItems(@NonNull List<OrderItem> orderItems,
                                                      @NonNull Currency baseCurrency) {
        return orderItems
            .stream()
            .map(item -> item.toBuilder()
                .id(randomUUID())
                .exchPrice(currencyService.exchange(item.price(), item.currency(), baseCurrency))
                .build())
            .collect(toList());
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
