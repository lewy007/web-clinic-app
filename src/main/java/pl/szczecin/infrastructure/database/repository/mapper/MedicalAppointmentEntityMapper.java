package pl.szczecin.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.infrastructure.database.entity.MedicalAppointmentEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MedicalAppointmentEntityMapper {
    MedicalAppointment mapFromEntity(MedicalAppointmentEntity medicalAppointmentEntity);

    MedicalAppointmentEntity mapToEntity(MedicalAppointment medicalAppointment);
}
