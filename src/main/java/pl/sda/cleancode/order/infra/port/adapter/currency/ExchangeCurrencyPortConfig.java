package pl.sda.cleancode.order.infra.port.adapter.currency;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.sda.cleancode.order.infra.port.ExchangeCurrencyPort;

@Configuration
class ExchangeCurrencyPortConfig {

    @Bean
    ExchangeCurrencyPort exchangeCurrencyPort() {
        return new ExchangeCurrencyPortAdapter();
    }
}
