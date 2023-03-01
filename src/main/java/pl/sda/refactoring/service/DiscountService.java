package pl.sda.refactoring.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DiscountService {
    public Discount getDiscount(String coupon) {
        throw new InfrastructureException("discount service failure");
    }

    public void deactivate(String coupon, UUID customerId) {
        throw new InfrastructureException("discount service failure");
    }

    public record Discount(double value) {
    }
}
