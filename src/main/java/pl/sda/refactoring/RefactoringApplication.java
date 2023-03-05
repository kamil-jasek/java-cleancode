package pl.sda.refactoring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import pl.sda.refactoring.service.OrderSettings;

@SpringBootApplication
@EnableConfigurationProperties({OrderSettings.class})
public class RefactoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(RefactoringApplication.class, args);
    }
}
