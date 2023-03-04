package pl.sda.refactoring.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerValidator {

    private final CustomerService customerService;

    void assertCustomerExists(@NonNull UUID customerId) {
        if (!customerService.exists(customerId)) {
            throw new CustomerNotFoundException("customer not found " + customerId);
        }
    }
}