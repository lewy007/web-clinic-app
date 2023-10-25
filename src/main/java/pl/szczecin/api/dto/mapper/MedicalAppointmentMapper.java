package pl.szczecin.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.Patient;

@Mapper(componentModel = "spring")
public interface MedicalAppointmentMapper extends OffsetDateTimeMapper {

    @Mapping(source = "patient.name",target = "patientName")
    @Mapping(source = "patient.surname",target = "patientSurname")
    @Mapping(source = "medicalAppointmentDate.dateTime", target = "dateTime", qualifiedByName = "mapOffsetDateTimeToString")
    MedicalAppointmentDTO map(MedicalAppointment medicalAppointment);

}
