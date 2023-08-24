package pl.szczecin.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.OffsetDateTime;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface StringToOffsetDateTimeMapper {

//    DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Named("mapStringToOffsetDateTime")
    default OffsetDateTime mapStringToOffsetDateTime(String stringDateTime) {
        return Optional.ofNullable(stringDateTime)
                .map(OffsetDateTime::parse)
//                .map(odt -> odt.format(DATE_FORMAT))
                .orElse(null);
    }

//    @Named("mapOffsetDateTimeToString")
//    default String mapOffsetDateTimeToString(OffsetDateTime offsetDateTime) {
//        return Optional.ofNullable(offsetDateTime)
//                .map(odt -> offsetDateTime.atZoneSameInstant(ZoneOffset.UTC))
//                .map(odt -> odt.format(DATE_FORMAT))
//                .orElse(null);
//    }
}
