package pl.sda.refactoring.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService {
    public boolean exists(UUID customerId) {
        throw new InfrastructureException("customer service failure");
    }
}
