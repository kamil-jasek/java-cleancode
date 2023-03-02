package pl.sda.cleancode.order.infra.port.adapter.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.sda.cleancode.order.infra.port.MailPort;

@Configuration
class MailPortConfig {

    @Bean
    MailPort mailPort() {
        return new DummyMailPortAdapter();
    }
}
