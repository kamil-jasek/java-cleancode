package pl.sda.refactoring.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sda.refactoring.service.port.CustomerPort;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomerValidator {

    private final CustomerPort customerPort;

    void assertCustomerExists(@NonNull UUID customerId) {
        if (!customerPort.exists(customerId)) {
            throw new CustomerNotFoundException("customer not found " + customerId);
        }
    }
}