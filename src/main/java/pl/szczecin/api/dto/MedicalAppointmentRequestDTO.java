package pl.szczecin.api.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@With
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalAppointmentRequestDTO {

    @NotNull(message = "Patient name cannot be null")
    @NotBlank(message = "Patient name cannot be blank")
    private String patientName;

    private String patientSurname;
    @Size()
    @NotNull
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    private String patientPhone;

    @Email
    @NotNull(message = "Patient email cannot be null")
    @NotBlank(message = "Patient email cannot be blank")
    private String patientEmail;

    private String patientAddressCountry;

    private String patientAddressCity;

    private String patientAddressPostalCode;

    private String patientAddressStreet;

    // jaki doktor i data wizyty
    private String doctorEmail;

    private String doctorSurname;

    private String doctorNote;

    private String medicalAppointmentDate;

    // password dla nowego pacjenta
    private String password;


    // metoda zbedna w praktyce, napisana, zeby za kazdym razem nie wklikiwac nowego usera od zera
    public static MedicalAppointmentRequestDTO buildDefaultData() {
        return MedicalAppointmentRequestDTO.builder()
                .patientName("Stefan")
                .patientSurname("Chorowity")
                .patientPhone("+48 754 552 234")
                .patientEmail("stefan.chorowity@clinic.pl")
                .patientAddressCountry("Polska")
                .patientAddressCity("Wroclaw")
                .patientAddressPostalCode("50-001")
                .patientAddressStreet("ul. Woronicza 15")
                .password("test")
                .build();
    }

    // metoda wykorzystana do sprawdzenia  walidacji danych wejsciowych -> RegistrationControllerWebMvcTest
    // Mapa z polami klasy, jesli istnieje dane pole to dodajemy do mapy jako klucz nazwe pola a wartosc w postaci Stringa
    public Map<String, String> asMap() {
        Map<String, String> result = new HashMap<>();
        Optional.ofNullable(patientName).ifPresent(elem -> result.put("patientName", elem));
        Optional.ofNullable(patientSurname).ifPresent(elem -> result.put("patientSurname", elem));
        Optional.ofNullable(patientPhone).ifPresent(elem -> result.put("patientPhone", elem));
        Optional.ofNullable(patientEmail).ifPresent(elem -> result.put("patientEmail", elem));
        Optional.ofNullable(patientAddressCountry).ifPresent(elem -> result.put("patientAddressCountry", elem));
        Optional.ofNullable(patientAddressCity).ifPresent(elem -> result.put("patientAddressCity", elem));
        Optional.ofNullable(patientAddressPostalCode).ifPresent(elem -> result.put("patientAddressPostalCode", elem));
        Optional.ofNullable(patientAddressStreet).ifPresent(elem -> result.put("patientAddressStreet", elem));
        Optional.ofNullable(password).ifPresent(elem -> result.put("password", elem));
        return result;
    }
}
