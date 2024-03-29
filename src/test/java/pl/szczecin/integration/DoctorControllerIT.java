package pl.szczecin.integration;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.szczecin.integration.configuration.AbstractIT;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoctorControllerIT extends AbstractIT {

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
    @DisplayName("That IT should return correct view of Doctor Controller")
    void doctorPageWorksCorrectly() {

        String url = "http://localhost:%s%s/doctor".formatted(port, basePath);
        ResponseEntity<String> response = this.testRestTemplate.getForEntity(url, String.class);

//        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).contains("Check the available dates");
//        Assertions.assertThat(response.getBody()).contains("Could not find a resource");

    }
}

