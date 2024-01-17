package pl.szczecin.infrastructure.nfz;

import org.springframework.stereotype.Component;
import pl.szczecin.domain.NfzProvider;
import pl.zajavka.infrastructure.nfz.model.DictionaryProviderEntry;

import java.util.Optional;

@Component
public class NfzProviderMapper {

    public NfzProvider map(Integer year, DictionaryProviderEntry attributes) {
        var builder = NfzProvider.builder()
                .year(year);

        Optional.ofNullable(attributes)
                .ifPresent(dto -> builder
                        .nip(dto.getAttributes().getNip())
                        .branch(dto.getAttributes().getBranch())
                        .commune(dto.getAttributes().getCommune())
                        .postCode(dto.getAttributes().getPostCode())
                        .regon(dto.getAttributes().getRegon())
                        .street(dto.getAttributes().getStreet())
                        .registryNumber(dto.getAttributes().getRegistryNumber())
                        .name(dto.getAttributes().getName())
                        .phone(dto.getAttributes().getPhone())
                        .place(dto.getAttributes().getPlace())
                        .code(dto.getAttributes().getCode())
                );
        return builder.build();
    }
}
