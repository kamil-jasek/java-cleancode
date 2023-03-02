package pl.sda.refactoring.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private UUID id;
    private Instant ctime;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    private UUID customerId;
    @Enumerated(EnumType.STRING)
    private Currency currency;
    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> items;
    private BigDecimal totalExch;
    private BigDecimal discount;
    private BigDecimal discountedTotal;
    private BigDecimal delivery;
}
