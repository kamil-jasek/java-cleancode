package pl.sda.refactoring.service.port.adapter.discount;

import org.springframework.stereotype.Service;
import pl.sda.refactoring.service.InfrastructureException;
import pl.sda.refactoring.service.port.DiscountPort;

import java.util.UUID;

@Service
final class DiscountService implements DiscountPort {
    @Override
    public Discount getDiscount(String coupon) {
        throw new InfrastructureException("discount service failure");
    }

    @Override
    public void deactivate(String coupon, UUID customerId) {
        throw new InfrastructureException("discount service failure");
    }
}
