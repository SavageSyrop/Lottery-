package ru.lot.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.lot.service.DrawTaskSchedulerService;

@Configuration
public class FlywayInitializerCustom {

    @Value("${flyway.url}")
    private String url;
    @Value("${flyway.user}")
    private String username;
    @Value("${flyway.password}")
    private String password;

    private final DrawTaskSchedulerService scheduler;

    public FlywayInitializerCustom(DrawTaskSchedulerService scheduler) {
        this.scheduler = scheduler;
    }

    public void initialize() {
        Flyway.configure()
                .dataSource(url, username, password)
                .cleanDisabled(true)
                .locations("classpath:db.migration")
                .load()
                .migrate();

        scheduler.init();
    }

    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
        };
    }
}
