package pl.szczecin.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.szczecin.api.dto.MedicalAppointmentDTO;
import pl.szczecin.domain.MedicalAppointmentRequest;

@Mapper(componentModel = "spring")
public interface MedicalAppointmentMapper {
    MedicalAppointmentRequest map(MedicalAppointmentDTO medicalAppointmentDTO);
}
