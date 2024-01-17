package pl.szczecin.infrastructure.nfz;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.szczecin.business.dao.NfzProviderDAO;
import pl.szczecin.domain.NfzProvider;
import pl.szczecin.domain.exception.NotFoundException;
import pl.zajavka.infrastructure.nfz.api.InfoApi;
import pl.zajavka.infrastructure.nfz.api.SownikiApi;
import pl.zajavka.infrastructure.nfz.api.UmowyApi;
import pl.zajavka.infrastructure.nfz.model.DictionaryProviderEntry;
import pl.zajavka.infrastructure.nfz.model.DictionaryProviderEntryListData;
import pl.zajavka.infrastructure.nfz.model.DictionaryProviderEntryListResponse;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class NfzClientImpl implements NfzProviderDAO {

    //Numer oddziału NFZ z którym została podpisana umowa (województwa od 1 do 16)
    private static final String BRANCH_NUMBER = "16";

    private final UmowyApi umowyApi;
    private final SownikiApi sownikiApi;
    private final NfzProviderMapper nfzProviderMapper;


    @Override
    public List<NfzProvider> getNfzProviders(Integer year) {

        // pobieramy listę providerów przemapowaną na DictionaryProviderEntries
        var dictionaryProviderEntries = getDictionaryProviderEntries(year);

        // mapujemy obiekty zewnetrznego api na obiekty naszej aplikacji
        return dictionaryProviderEntries.stream()
                .map(elem -> nfzProviderMapper.map(year, elem))
                .toList();
    }


    private List<DictionaryProviderEntry> getDictionaryProviderEntries(Integer year) {
        return Optional.ofNullable(getEntryListResponse(year))
                .map(DictionaryProviderEntryListResponse::getData)
                .map(DictionaryProviderEntryListData::getEntries)
                .orElseThrow(
                        () -> new NotFoundException("Could not find a provider for year: [%s]".formatted(year)));

    }

    // niektore z parametrow sa defaultowe, wiec wpisujemy nulle
    private DictionaryProviderEntryListResponse getEntryListResponse(Integer year) {
        return sownikiApi
                .getAllDProviders(year,
                        BRANCH_NUMBER,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        1,
                        25,
                        "json",
                        null)
                .block();
    }

}
