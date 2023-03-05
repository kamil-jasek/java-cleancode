package pl.sda.refactoring.service.port;

import java.util.UUID;

public interface CustomerPort {
    boolean exists(UUID customerId);
}
