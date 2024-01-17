package pl.szczecin.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.szczecin.business.dao.NfzProviderDAO;
import pl.szczecin.domain.NfzProvider;
import pl.szczecin.domain.exception.ProcessingException;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class NfzService {

    private final NfzProviderDAO nfzProviderDAO;

    public List<NfzProvider> getNfzProviders(final Integer year) {
        log.debug("Looking for NFZ list providers, in year: [{}]", year);

        List<NfzProvider> nfzProviders = nfzProviderDAO.getNfzProviders(year);
        if (nfzProviders.isEmpty()) {
            throw new ProcessingException(
                    "NFZ returned empty list for this year: [%s]".formatted(year)
            );
        }

        return nfzProviders;

        //TODO logika dla losowego elementu ze zbioru, nie wykorzystana w aplikacji

//        return Optional.of(nfzProviders.get(new Random().nextInt(nfzProviders.size())))
//                .map(anyProvider -> nfzProviderDAO.getProvider(anyProvider.getBranch()))
//                .orElseThrow(() -> new NotFoundException("Could not find random NFZ provider"));

//       return nfzProviders.stream().findAny()
//               .orElseThrow(() -> new NotFoundException("Could not find random NFZ provider"));
    }

}
