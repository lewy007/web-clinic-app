package pl.szczecin.business.dao;

import pl.szczecin.domain.NfzProvider;

import java.util.List;

public interface NfzProviderDAO {

    List<NfzProvider> getNfzProviders(final Integer year);
}
