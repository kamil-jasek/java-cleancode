package pl.sda.refactoring.service.port.adapter.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface OrderRepo extends JpaRepository<OrderEntity, UUID> {
}
