package pl.szczecin.domain;

import lombok.Builder;
import lombok.Value;
import lombok.With;

import java.time.OffsetDateTime;

// Klasa sluzy do zainicjowania umówienia wizyty

@With
@Value
@Builder
public class MedicalAppointmentRequest {

    String existingPatientEmail;
    String patientName;
    String patientSurname;
    String patientPhone;
    String patientEmail;
    String patientAddressCountry;
    String patientAddressCity;
    String patientAddressPostalCode;
    String patientAddressStreet;
    String doctorEmail;
    String doctorSurname;
    OffsetDateTime medicalAppointmentDate;
}
