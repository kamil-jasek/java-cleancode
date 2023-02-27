package pl.sda.cleancode.order.infra.port.adapter.repo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import pl.sda.cleancode.order.domain.*;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.sda.cleancode.order.domain.Currency.*;
import static pl.sda.cleancode.order.domain.WeightUnit.KG;
import static pl.sda.cleancode.order.domain.WeightUnit.LB;

@DataJpaTest
@ContextConfiguration(classes = JpaOrderRepoConfig.class)
class JpaOrderRepoPortAdapterTest {

    @Autowired
    private JpaOrderRepoPortAdapter repoPort;

    @Test
    void should_save_and_retrieve_order() {
        // given an order
        final var order = new Order(
            OrderId.of("aaf64a42-b089-4a2f-a371-397ee6a4eba0"),
            Instant.parse("2022-02-20T10:00:01Z"),
            CustomerId.of("a3270154-62c4-47fc-8f13-f8767608450a"),
            new ExchangedOrderItemList(
                USD,
                new ExchangedOrderItem(
                    new OrderItem(
                        ProductId.of("abeffc80-71b8-43d0-9b57-bdda6f963625"),
                        Price.of("24.50", EUR),
                        Weight.of(0.20, LB),
                        Quantity.of(1)),
                    Price.of("25.34", USD)
                ),
                new ExchangedOrderItem(
                    new OrderItem(
                        ProductId.of("6e6a0cba-37a6-4c88-a098-710e2823c549"),
                        Price.of("32.50", PLN),
                        Weight.of(0.12, KG),
                        Quantity.of(3)
                    ),
                    Price.of("143.47", USD)
                )),
            Price.of("20.00", USD),
            Price.of("91.15", USD));

        // expect order is saved without exceptions
        repoPort.save(order);

        // and order can be retrieved
        final var retrievedOrder = repoPort.getOrder(order.id());
        // then orders are the same
        assertThat(order).isEqualTo(retrievedOrder);
    }
}