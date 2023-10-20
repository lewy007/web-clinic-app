package pl.szczecin.infrastructure.database.repository.jpa;

import lombok.AllArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import pl.szczecin.infrastructure.database.entity.PatientEntity;
import pl.szczecin.integration.configuration.PersistenceContainerTestConfiguration;
import pl.szczecin.util.EntityFixtures;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
// nie chcemy korzystac z wbudowanej bazy danych, czyli H2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class PatientJpaRepositoryTest {

    private PatientJpaRepository patientJpaRepository;

    @Test
    @DisplayName("That method should return correct patient entity by email")
    void shouldReturnCorrectPatientEntityByEmail() {
        //given
        PatientEntity expectedPatientEntity = EntityFixtures.somePatientEntity1();
        patientJpaRepository.saveAndFlush(expectedPatientEntity);

        //when
        PatientEntity resultPatientEntity
                = patientJpaRepository.findByEmail(expectedPatientEntity.getEmail());
        //then
        Assertions.assertThat(resultPatientEntity).isEqualTo(expectedPatientEntity);
    }

}