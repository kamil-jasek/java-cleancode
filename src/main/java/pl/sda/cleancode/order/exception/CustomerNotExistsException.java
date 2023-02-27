package pl.sda.cleancode.order.exception;

import pl.sda.cleancode.application.exception.DomainIllegalArgumentException;
import pl.sda.cleancode.order.domain.CustomerId;

public final class CustomerNotExistsException extends DomainIllegalArgumentException {

    public CustomerNotExistsException(CustomerId customerId) {
        super("customer not exists with id: " + customerId.id());
    }
}
