package pl.szczecin.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.szczecin.api.dto.MedicalAppointmentDateDTO;
import pl.szczecin.domain.MedicalAppointmentDate;

@Mapper(componentModel = "spring")
public interface MedicalAppointmentDateMapper extends OffsetDateTimeMapper {
    MedicalAppointmentDateDTO map(MedicalAppointmentDate medicalAppointmentDate);
}
