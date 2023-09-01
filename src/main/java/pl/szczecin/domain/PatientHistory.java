package pl.szczecin.domain;

import lombok.Builder;
import lombok.ToString;
import lombok.Value;
import lombok.With;

import java.time.OffsetDateTime;
import java.util.List;

@With
@Value
@Builder
@ToString(of = "patientEmail")
public class PatientHistory {

    String patientEmail;
    List<MedicalAppointment> medicalAppointments;


    @Value
    @Builder
    @ToString(of = {"doctorNote"})
    public static class MedicalAppointment {
        String doctorNote;
        OffsetDateTime dateTime;
        String doctorName;
        String doctorSurname;
    }

}
