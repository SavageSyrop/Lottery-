package ru.lot;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.lot.configuration.FlywayInitializer;

@Slf4j
@SpringBootApplication(scanBasePackages = "ru.lot")
@EnableTransactionManagement
@EnableJpaRepositories("ru.lot")
public class Application {
    public static void main(String[] args) {

        log.info("Application stated!");

        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        context.getBean(FlywayInitializer.class).initialize();
    }
}
