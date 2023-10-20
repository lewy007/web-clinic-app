package pl.szczecin.integration;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import pl.szczecin.integration.configuration.AbstractIT;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HomeControllerIT extends AbstractIT {

    // do sprawdzenia na jakim faktycznie porcie zostala uruchomiona apka
    @LocalServerPort
    private int port;

    // @Value odwoluje sie do zmiennej w application.yml,
    // czyli do zmiennej basePath zostanie przypisana zmienna z pliku, czyli /car-dealership
    @Value("${server.servlet.context-path}")
    private String basePath;


    // symuluje dzialanie przegladarki. Obiekt wysyla request do serwera jako klient
    private final TestRestTemplate testRestTemplate;


    @Test
    @DisplayName("That IT should return correct view of Home Controller")
    void homePageWorksCorrectly() {

        String url = "http://localhost:%s%s".formatted(port, basePath);
        String page = this.testRestTemplate.getForObject(url, String.class);

        Assertions.assertThat(page).contains("Web Clinic Application");

    }
}

