package pl.szczecin.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
import pl.szczecin.domain.MedicalAppointmentDate;

@Mapper(componentModel = "spring")
public interface MedicalAppointmentDateMapper extends OffsetDateTimeMapper {


    @Mapping(source = "dateTime", target = "dateTime", qualifiedByName = "mapOffsetDateTimeToString")
    MedicalAppointmentDateDTO map(MedicalAppointmentDate medicalAppointmentDate);
}
