package pl.sda.cleancode.order.infra.port;

import pl.sda.cleancode.order.domain.CustomerId;

public interface CustomerPort {
    boolean customerExists(CustomerId customerId);
}
