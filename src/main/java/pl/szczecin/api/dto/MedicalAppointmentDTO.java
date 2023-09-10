package pl.szczecin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.domain.Patient;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedicalAppointmentDTO {

    private String doctorNote;
    private Patient patient;
    private MedicalAppointmentDate medicalAppointmentDate;

    // data do wyswietlenia Terminarza dla lekarza
    private String dateTime;

}
