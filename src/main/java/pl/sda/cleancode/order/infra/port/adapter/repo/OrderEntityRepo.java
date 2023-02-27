package pl.sda.cleancode.order.infra.port.adapter.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface OrderEntityRepo extends JpaRepository<OrderEntity, UUID> {
}
