package pl.szczecin.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.OffsetDateTime;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface StringToOffsetDateTimeMapper {


    @Named("mapStringToOffsetDateTime")
    default OffsetDateTime mapStringToOffsetDateTime(String stringDateTime) {
        return Optional.ofNullable(stringDateTime)
                .map(OffsetDateTime::parse)
                .orElse(null);
    }

}
