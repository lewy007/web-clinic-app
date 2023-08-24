package pl.szczecin.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.With;

import java.time.OffsetDateTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "medicalAppointmentDateId")
@ToString(of = {"medicalAppointmentDateId", "dateTime", "status"})
public class MedicalAppointmentDate {

    Integer medicalAppointmentDateId;
    OffsetDateTime dateTime;
    Boolean status;
    Doctor doctor;
}
