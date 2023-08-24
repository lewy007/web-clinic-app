package pl.szczecin.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.szczecin.domain.Patient;
import pl.szczecin.infrastructure.database.entity.PatientEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PatientEntityMapper {

    PatientEntity mapToEntity(Patient patient);

    @Mapping(target = "address.patient", ignore = true)
    Patient mapFromEntity(PatientEntity patientEntity);
}
