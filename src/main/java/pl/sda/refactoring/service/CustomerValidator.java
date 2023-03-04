package pl.sda.refactoring.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.sda.refactoring.service.command.MakeOrder;

@Component
@RequiredArgsConstructor
public class CustomerValidator {

    private final CustomerService customerService;

    void assertCustomerExists(MakeOrder cmd) {
        if (!customerService.exists(cmd.customerId())) {
            throw new CustomerNotFoundException("customer not found " + cmd.customerId());
        }
    }
}