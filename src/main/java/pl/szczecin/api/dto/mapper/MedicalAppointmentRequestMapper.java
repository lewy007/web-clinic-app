package pl.szczecin.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.szczecin.api.dto.MedicalAppointmentRequestDTO;
import pl.szczecin.domain.MedicalAppointmentRequest;

@Mapper(componentModel = "spring")
public interface MedicalAppointmentRequestMapper extends OffsetDateTimeMapper {

    @Mapping(source = "medicalAppointmentDate", target = "medicalAppointmentDate", qualifiedByName = "mapStringToOffsetDateTime")
    MedicalAppointmentRequest map(MedicalAppointmentRequestDTO medicalAppointmentRequestDTO);

}
