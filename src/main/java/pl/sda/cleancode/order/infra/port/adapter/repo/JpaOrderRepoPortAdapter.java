package pl.sda.cleancode.order.infra.port.adapter.repo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.sda.cleancode.order.domain.Order;
import pl.sda.cleancode.order.domain.OrderId;
import pl.sda.cleancode.order.infra.port.OrderRepoPort;

@RequiredArgsConstructor
final class JpaOrderRepoPortAdapter implements OrderRepoPort {

    private final @NonNull OrderEntityRepo repo;
    private final @NonNull OrderEntityMapper mapper;

    @Override
    public Order getOrder(OrderId orderId) {
        return mapper.toDomain(repo.getReferenceById(orderId.id()));
    }

    @Override
    public void save(Order order) {
        repo.save(mapper.toEntity(order));
    }
}
