package pl.sda.refactoring.service.port.adapter.customer;

import org.springframework.stereotype.Service;
import pl.sda.refactoring.service.port.CustomerPort;
import pl.sda.refactoring.service.InfrastructureException;

import java.util.UUID;

@Service
final class CustomerDatabase implements CustomerPort {
    @Override
    public boolean exists(UUID customerId) {
        throw new InfrastructureException("customer service failure");
    }
}
