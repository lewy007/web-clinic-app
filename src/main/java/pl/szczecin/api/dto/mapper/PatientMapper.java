package pl.szczecin.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.szczecin.api.dto.PatientDTO;
import pl.szczecin.domain.Patient;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDTO map(Patient patient);
}
