package pl.sda.refactoring.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    private UUID id;
    private UUID productId;
    private BigDecimal price;
    private Currency currency;
    private Double weight;
    private WeightUnit weightUnit;
    private int quantity;
    private BigDecimal exchPrice;
}
