package pl.szczecin.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.szczecin.api.dto.DoctorDTO;
import pl.szczecin.domain.Doctor;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    DoctorDTO map(Doctor doctor);
}
