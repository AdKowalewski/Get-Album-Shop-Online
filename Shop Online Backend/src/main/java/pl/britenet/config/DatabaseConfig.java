package pl.britenet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.britenet.database.DatabaseService;

@Configuration
public class DatabaseConfig {

    @Bean
    public DatabaseService getDatabaseService() {
        return new DatabaseService();
    }
}
