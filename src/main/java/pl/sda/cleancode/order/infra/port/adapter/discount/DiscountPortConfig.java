package pl.sda.cleancode.order.infra.port.adapter.discount;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.sda.cleancode.order.infra.port.DiscountPort;

@Configuration
class DiscountPortConfig {

    @Bean
    DiscountPort discountPort() {
        return new DummyDiscountPortAdapter();
    }
}
