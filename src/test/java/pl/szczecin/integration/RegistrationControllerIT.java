package pl.szczecin.integration;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.szczecin.integration.configuration.AbstractIT;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RegistrationControllerIT extends AbstractIT {

    // do sprawdzenia na jakim faktycznie porcie zostala uruchomiona apka
    @LocalServerPort
    private int port;

    // @Value odwoluje sie do zmiennej w application.yml,
    // czyli do zmiennej basePath zostanie przypisana zmienna z pliku, czyli /web-clinic-application
    @Value("${server.servlet.context-path}")
    private String basePath;


    // symuluje dzialanie przegladarki. Obiekt wysyla request do serwera jako klient
    private final TestRestTemplate testRestTemplate;


    @Test
    @DisplayName("That IT should return correct view of GET method in Registration Controller")
    void registrationPageWorksCorrectly() {

        String url = "http://localhost:%s%s/registration".formatted(port, basePath);
        String page = this.testRestTemplate.getForObject(url, String.class);

        Assertions.assertThat(page)
                .contains("If you are new patient - please fill out every input in the form!");

    }

    @Test
    @DisplayName("That IT should return correct status and body of POST method in Registration Controller")
    void makeRegistrationPageWorksCorrectly() throws URISyntaxException {

        String url = "http://localhost:%s%s/registration".formatted(port, basePath);
        URI uri = new URI(url);

        // Utwórz obiekt `MultiValueMap` do przekazywania danych jako formularza
        MultiValueMap<String, String> requestParameters = getRequestToRegistrationForm();

        // Utwórz obiekt `HttpHeaders` i ustaw odpowiedni nagłówek, jeśli to konieczne
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Utwórz obiekt `HttpEntity` z przekazywanymi danymi i nagłówkami
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestParameters, headers);

        // Wykonaj zapytanie HTTP POST
        ResponseEntity<String> result
                = this.testRestTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);

        // Sprawdź status odpowiedzi i treść odpowiedzi
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(Objects.requireNonNull(result.getBody())
                .contains("You have registered in Web Clinic Application"));

    }

    @NotNull
    private static MultiValueMap<String, String> getRequestToRegistrationForm() {
        MultiValueMap<String, String> variables = new LinkedMultiValueMap<>();
        variables.add("patientName", "Wiktor");
        variables.add("patientSurname", "Wolski");
        variables.add("patientPhone", "+48 300 300 300");
        variables.add("patientAddressCountry", "Poland");
        variables.add("patientAddressCity", "Warsaw");
        variables.add("patientAddressPostalCode", "20-300");
        variables.add("patientAddressStreet", "ul. Maciejkowa 12");
        variables.add("patientEmail", "lala@wp.pl");
        variables.add("password", "password");
        return variables;
    }

//    @Test
//    @DisplayName("That IT should return correct view of POST method in Registration Controller")
//    void makeRegistrationPageWorksCorrectly() throws URISyntaxException {
//
//        String url = "http://localhost:%s%s/registration".formatted(port, basePath);
//        URI uri = new URI(url);
//        MedicalAppointmentRequestDTO medicalAppointmentRequestDTO
//                = EntityFixtures.someMedicalAppointmentRequestDTO();
//
////        HttpHeaders headers = new HttpHeaders();
////        headers.set("registration_done", "true");
//
//        HttpEntity<MedicalAppointmentRequestDTO> request
//                = new HttpEntity<>(medicalAppointmentRequestDTO);
//
//        ResponseEntity <String> result
//                = this.testRestTemplate.postForEntity(uri, request, String.class);
//
//        Assert.assertEquals(201, result.getStatusCodeValue());
////        Assert.assertEquals(true, result.getBody().contains("Missing request header"));
////        Assertions.assertThat(result)
////                .contains("You have registered in Web Clinic Application!");
//
//    }
}

