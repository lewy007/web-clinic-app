package pl.szczecin.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientHistoryDTO {

    String patientEmail;
    List<MedicalAppointmentDTO> medicalAppointments;

    public static PatientHistoryDTO buildDefault() {
        return PatientHistoryDTO.builder()
                .patientEmail("empty")
                .medicalAppointments(Collections.emptyList())
                .build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MedicalAppointmentDTO implements Comparable<MedicalAppointmentDTO> {
        private String doctorNote;
        private String dateTime;
        private String doctorName;
        private String doctorSurname;

        @Override
        public int compareTo(MedicalAppointmentDTO o) {
            return this.dateTime.compareTo(o.dateTime);
        }
    }
}
