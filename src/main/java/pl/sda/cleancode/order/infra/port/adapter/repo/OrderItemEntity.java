package pl.sda.cleancode.order.infra.port.adapter.repo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@AllArgsConstructor
@NoArgsConstructor // for JPA
@Getter
class OrderItemEntity {

    @Id
    private UUID id;
    private UUID productId;
    private BigDecimal originalPrice;
    private String originalCurrency;
    private BigDecimal exchangedPrice;
    private double weight;
    private String weightUnit;
    private int quantity;
}
