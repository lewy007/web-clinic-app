package pl.szczecin.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pl.szczecin.api.dto.PatientDTO;
import pl.szczecin.api.dto.PatientHistoryDTO;
import pl.szczecin.domain.Patient;
import pl.szczecin.domain.PatientHistory;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper extends OffsetDateTimeMapper {

    PatientDTO map(Patient patient);


    @Mapping(source = "medicalAppointments", target = "medicalAppointments", qualifiedByName = "mapMedicalAppointments")
    PatientHistoryDTO map(PatientHistory patientHistory);


    @Named("mapMedicalAppointments")
    default List<PatientHistoryDTO.MedicalAppointmentDTO> mapMedicalAppointments(
            List<PatientHistory.MedicalAppointment> requests
    ) {
        return requests.stream().map(this::mapMedicalAppointment).toList();
    }


    @Mapping(source = "dateTime", target = "dateTime", qualifiedByName = "mapOffsetDateTimeToString")
    PatientHistoryDTO.MedicalAppointmentDTO mapMedicalAppointment(PatientHistory.MedicalAppointment medicalAppointment);
}
