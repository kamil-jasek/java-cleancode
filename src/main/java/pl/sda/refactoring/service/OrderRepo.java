package pl.sda.refactoring.service;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.refactoring.entity.Order;

import java.util.UUID;

public interface OrderRepo extends JpaRepository<Order, UUID> {
}
