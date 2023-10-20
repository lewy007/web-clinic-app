package pl.szczecin.integration.configuration;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

// Klasa sluzy do stworzenia tymczasowej bazy danych postgresql przy wykorzystaniu Testcontainers
// na czas trwania testu - jest to inny sposob na stworzenie konfiguracji kontenera
// (pierwszy jest w infrastructure - DatabaseContainerInitializer) - efekt ten sam
@TestConfiguration
public class PersistenceContainerTestConfiguration {

    public static final String POSTGRES_USERNAME = "test";
    public static final String POSTGRES_PASSWORD = "test";
    public static final String POSTGRESQL = "postgres";
    public static final String POSTGRES_CONTAINER = "postgres:15.0";

    @Bean
    @Qualifier(POSTGRESQL)
    PostgreSQLContainer<?> postgreSQLContainer() {
        PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_CONTAINER)
                .withUsername(POSTGRES_USERNAME)
                .withPassword(POSTGRES_PASSWORD);
        postgreSQLContainer.start();
        return postgreSQLContainer;
    }

    @Bean
    DataSource dataSource(final PostgreSQLContainer<?> postgreSQLContainer) {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .build();
    }
}
