package pl.sda.cleancode.order.infra.port.adapter.customer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.sda.cleancode.order.infra.port.CustomerPort;

@Configuration
class CustomerPortConfig {

    @Bean
    CustomerPort customerPort() {
        return new DummyCustomerPortAdapter();
    }
}
