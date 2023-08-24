package pl.szczecin.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalAppointmentDTO {

    // jesli klient przychodzi kolejny raz to mamy go w bazie danych i wystarczy tylko email
    @Email
    private String existingPatientEmail;

    // jesli przychodzi pierwszy raz to potrzebujmey wszytskich danych
    private String patientName;
    private String patientSurname;
    @Size()
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    private String patientPhone;
    @Email
    private String patientEmail;
    private String patientAddressCountry;
    private String patientAddressCity;
    private String patientAddressPostalCode;
    private String patientAddressStreet;

    // jaki samochod i kto sprzedal
    private String doctorPesel;
    private OffsetDateTime medicalAppointmentDate;


    // metoda zbedna w praktyce, napisana, zeby za kazdym razem nie wklikiwac nowego usera od zera
    public static MedicalAppointmentDTO buildDefaultData() {
        return MedicalAppointmentDTO.builder()
                .patientName("Alfred")
                .patientSurname("Samochodowy")
                .patientPhone("+48 754 552 234")
                .patientEmail("alf.samoch@gmail.com")
                .patientAddressCountry("Polska")
                .patientAddressCity("Wrocław")
                .patientAddressPostalCode("50-001")
                .patientAddressStreet("Bokserska 15")
                .build();
    }
}
