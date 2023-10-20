package pl.szczecin.integration.configuration;

import org.junit.jupiter.api.AfterEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import pl.szczecin.WebClinicApplication;

// klasa sluzy do stworzenia SpringBootTest, ktore beda podnosily caly kontekst i testowaly cala aplikacje

@ActiveProfiles("test") // aplikacja na potrzeby testu zostanie uruchomiona ze Spring Profilem=test
@Import(PersistenceContainerTestConfiguration.class)
@SpringBootTest(
        classes = {WebClinicApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT  // apka uruchamiana na losowym porcie
)
public abstract class AbstractIT {

    @AfterEach
    void afterEach() {
        // Czasami przydatne jest czyscic dane po kazdym tescie i odbudowywac wszystko od nowa,
        // kwestia przypadku i kontekstu
    }
}
