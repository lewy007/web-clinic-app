package pl.szczecin.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.domain.MedicalAppointment;

@Mapper(componentModel = "spring")
public interface MedicalAppointmentMapper extends OffsetDateTimeMapper {

    @Mapping(source = "medicalAppointmentDate.dateTime", target = "dateTime", qualifiedByName = "mapOffsetDateTimeToString")
    MedicalAppointmentDTO map(MedicalAppointment medicalAppointment);

}
