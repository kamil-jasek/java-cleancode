package pl.sda.refactoring.service;

import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sda.refactoring.entity.Currency;
import pl.sda.refactoring.entity.Order;
import pl.sda.refactoring.entity.OrderItem;
import pl.sda.refactoring.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.math.RoundingMode.HALF_UP;
import static java.util.UUID.randomUUID;

@Service
@Transactional
public class OrderService {

    private final DiscountService discountService;
    private final CurrencyService currencyService;
    private final CustomerService customerService;
    private final OrderRepo orderRepo;
    private final EmailService emailService;
    private final List<LocalDate> freeDeliveryDays;
    private final List<String> emailRecipients;

    public OrderService(@NonNull DiscountService discountService,
                        @NonNull CurrencyService currencyService,
                        @NonNull CustomerService customerService,
                        @NonNull OrderRepo orderRepo,
                        @NonNull EmailService emailService,
                        @NonNull OrderSettings settings) {
        this.discountService = discountService;
        this.currencyService = currencyService;
        this.customerService = customerService;
        this.orderRepo = orderRepo;
        this.emailService = emailService;
        this.freeDeliveryDays = settings.discountSettings().freeDeliveryDays();
        this.emailRecipients = settings.emailRecipients();
    }

    public UUID makeOrder(@NonNull UUID customerId,
                          @NonNull List<OrderItem> orderItems,
                          @NonNull Currency currency,
                          String coupon) {
        boolean exists = customerService.exists(customerId);
        if (exists) {
            if (!orderItems.isEmpty()) {
                // exchange currencies in order items
                List<OrderItem> exchItems = new ArrayList<>();
                for (var item : orderItems) {
                    var exchItem = item.toBuilder()
                        .id(UUID.randomUUID())
                        .exchPrice(currencyService.exchange(item.price(), item.currency(), currency))
                        .build();
                    exchItems.add(exchItem);
                }

                // calculate delivery
                var tp = BigDecimal.ZERO;
                var twInGrams = 0;
                for (var item : exchItems) {
                    tp = tp.add(item
                            .exchPrice()
                            .multiply(BigDecimal.valueOf(item.quantity())))
                        .setScale(2, HALF_UP);
                    switch (item.weightUnit()) {
                        case GM -> twInGrams += item.weight();
                        case KG -> twInGrams += item.weight() * 1000;
                        case LB -> twInGrams += item.weight() * 453.59237;
                    }
                }
                var deliveryPrice = BigDecimal.ZERO;
                // delivery costs are in USD
                var tpInUsd = currencyService.exchange(tp, currency, Currency.USD);
                if (tpInUsd.compareTo(new BigDecimal("400.00")) > 0 && twInGrams < 2000) {
                    deliveryPrice = new BigDecimal("0.00"); // free delivery
                } else if (tpInUsd.compareTo(new BigDecimal("200.00")) > 0 && twInGrams < 1000) {
                    deliveryPrice = new BigDecimal("12.00"); // first level
                } else if (tpInUsd.compareTo(new BigDecimal("100.00")) > 0 && twInGrams < 1000) {
                    deliveryPrice = new BigDecimal("15.00"); // second level
                } else {
                    // default delivery
                    deliveryPrice = new BigDecimal("20.00");
                }
                deliveryPrice = currencyService.exchange(deliveryPrice, Currency.USD, currency);

                // calculate discount
                var discountPrice = BigDecimal.ZERO;
                if (coupon != null) {
                    var discount = discountService.getDiscount(coupon);
                    if (discount != null) {
                        discountPrice = tp
                            .multiply(BigDecimal.valueOf(discount.value()))
                            .setScale(2, HALF_UP);
                        discountService.deactivate(coupon, customerId);
                    }
                }
                if (freeDeliveryDays.contains(LocalDate.now())) {
                    discountPrice = discountPrice.add(deliveryPrice);
                }

                var order = Order.builder()
                    .id(randomUUID())
                    .ctime(Instant.now())
                    .currency(currency)
                    .status(OrderStatus.CONFIRMED)
                    .customerId(customerId)
                    .items(exchItems)
                    .totalExch(tp)
                    .delivery(deliveryPrice)
                    .discount(discountPrice)
                    .discountedTotal(tp.add(deliveryPrice).subtract(discountPrice))
                    .build();
                orderRepo.save(order);
                sendEmail(order);
                return order.id();
            } else {
                throw new EmptyOrderItemsListException();
            }
        } else {
            throw new CustomerNotFoundException("customer not found " + customerId);
        }
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
