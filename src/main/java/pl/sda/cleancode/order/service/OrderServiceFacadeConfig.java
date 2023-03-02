package pl.sda.cleancode.order.service;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.sda.cleancode.application.event.DomainEventPublisher;
import pl.sda.cleancode.order.infra.port.*;

import java.time.Clock;
import java.util.List;

@Configuration
@EnableConfigurationProperties({
    DeliveryCostSettings.class,
    DiscountSettings.class
})
class OrderServiceFacadeConfig {

    @Bean
    OrderServiceFacade orderServiceFacade(CustomerPort customerPort,
                                          OrderRepoPort repoPort,
                                          Clock clock,
                                          ExchangeCurrencyPort currencyPort,
                                          DeliveryCostSettings deliveryCostSettings,
                                          DiscountPort discountPort,
                                          DiscountSettings discountSettings,
                                          DomainEventPublisher eventPublisher) {
        return new OrderServiceFacade(customerPort,
            repoPort,
            clock,
            new OrderItemCurrencyExchanger(currencyPort),
            new DeliveryCostCalculator(deliveryCostSettings, currencyPort),
            discountCalculator(discountPort, discountSettings, clock),
            eventPublisher);
    }

    private DiscountCalculator discountCalculator(DiscountPort discountPort,
                                                  DiscountSettings settings,
                                                  Clock clock) {
        return new CompositeDiscountCalculator(List.of(
            new OneTimeCouponDiscountCalculator(discountPort),
            new FreeDeliveryDiscountCalculator(settings.freeDeliveryDays(), clock)));
    }

    @Bean
    EmailNotificationEventHandler emailNotificationEventHandler(MailPort mailPort) {
        return new EmailNotificationEventHandler(mailPort);
    }
}
