package pl.szczecin.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.szczecin.domain.Patient;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.infrastructure.database.entity.PatientEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientEntityMapper {

    PatientEntity mapToEntity(Patient patient);

    @Mapping(target = "address.patient", ignore = true)
    Patient mapFromEntity(PatientEntity patientEntity);


    // metoda defaultowa ktorej implementacja nie jest tworzona przez biblioteke Mappera
    default PatientHistory mapFromEntity(PatientEntity entity, String patientEmail) {
        return PatientHistory.builder()
                .patientEmail(patientEmail)
                .medicalAppointments(entity.getMedicalAppointmentDetails().stream()
                        .map(request -> PatientHistory.MedicalAppointment.builder()
                                .dateTime(request.getMedicalAppointmentDateEntity().getDateTime())
                                .doctorName(request.getMedicalAppointmentDateEntity().getDoctor().getName())
                                .doctorSurname(request.getMedicalAppointmentDateEntity().getDoctor().getSurname())
                                .doctorEmail(request.getMedicalAppointmentDateEntity().getDoctor().getEmail())
                                .doctorNote(request.getDoctorNote())
                                .build())
                        .toList())
                .build();
    }

}
