package pl.szczecin.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.infrastructure.database.entity.MedicalAppointmentDateEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MedicalAppointmentDateEntityMapper {

    @Mapping(target = "doctor", ignore = true)
    MedicalAppointmentDate mapFromEntity(MedicalAppointmentDateEntity medicalAppointmentDateEntity);

    MedicalAppointmentDateEntity mapToEntity(MedicalAppointmentDate medicalAppointmentDate);
}
