package pl.szczecin.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.With;

@With
@Value
@Builder
@EqualsAndHashCode(of = "medicalAppointmentId")
@ToString(of = {"medicalAppointmentId", "doctorNote"})
public class MedicalAppointment {

    Integer medicalAppointmentId;
    String doctorNote;
    Patient patient;
    MedicalAppointmentDate medicalAppointmentDate;
}
