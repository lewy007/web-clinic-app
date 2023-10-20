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
import pl.szczecin.infrastructure.database.entity.DoctorEntity;
import pl.szczecin.integration.configuration.PersistenceContainerTestConfiguration;
import pl.szczecin.util.EntityFixtures;

import java.util.Optional;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
// nie chcemy korzystac z wbudowanej bazy danych, czyli H2
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PersistenceContainerTestConfiguration.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
class DoctorJpaRepositoryTest {

    private DoctorJpaRepository doctorJpaRepository;

    @Test
    @DisplayName("That method should return correct doctor entity by email")
    void shouldReturnCorrectDoctorEntityByEmail() {
        //given
        Optional<DoctorEntity> expectedDoctorEntity = EntityFixtures.someDoctorEntity1();

        //when
        Optional<DoctorEntity> resultDoctorEntity;
        if (expectedDoctorEntity.isPresent()) {
            resultDoctorEntity = doctorJpaRepository.findByEmail(expectedDoctorEntity.get().getEmail());
        } else {
            throw new IllegalStateException("Expected doctor entity is not present.");
        }

        //then
        Assertions.assertThat(resultDoctorEntity).isEqualTo(expectedDoctorEntity);
    }

}