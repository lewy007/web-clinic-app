package pl.szczecin.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.szczecin.api.dto.NfzProviderDTO;
import pl.szczecin.domain.NfzProvider;

@Mapper(componentModel = "spring")
public interface NfzProviderMapper {

    NfzProviderDTO map(NfzProvider nfzProvider);
}
