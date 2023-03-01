package pl.sda.refactoring.service;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDate;
import java.util.List;

@ConfigurationProperties("orders")
public record OrderSettings(DiscountSettings discountSettings,
                            List<String> emailRecipients) {

    record DiscountSettings(List<LocalDate> freeDeliveryDays) {
    }
}
