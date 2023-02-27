package pl.sda.cleancode.order.infra.port.adapter.repo;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@EntityScan
@Configuration
class JpaOrderRepoConfig {

    @Bean
    JpaOrderRepoPortAdapter orderRepoPort(OrderEntityRepo repo) {
        return new JpaOrderRepoPortAdapter(repo, new OrderEntityMapper());
    }
}
