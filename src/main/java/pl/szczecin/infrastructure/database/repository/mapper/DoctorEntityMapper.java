package pl.szczecin.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.szczecin.domain.Doctor;
import pl.szczecin.infrastructure.database.entity.DoctorEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DoctorEntityMapper {

    DoctorEntity mapToEntity(Doctor doctor);

    @Mapping(target = "appointmentsDate", ignore = true)
    Doctor mapFromEntity(DoctorEntity doctorEntity);
}
