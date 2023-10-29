package pl.szczecin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientHistoryDTO {

    String patientEmail;
    List<MedicalAppointmentDTO> medicalAppointments;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicalAppointmentDTO implements Comparable<MedicalAppointmentDTO> {
        private String doctorNote;
        private String dateTime;
        private String doctorName;
        private String doctorSurname;

        // dopisany ale nie działa, nie pobierany jest email
        private String doctorEmail;

        @Override
        public int compareTo(MedicalAppointmentDTO o) {
            return this.dateTime.compareTo(o.dateTime);
        }
    }
}
