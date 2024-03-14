package pl.szczecin.infrastructure.database.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szczecin.domain.Doctor;
import pl.szczecin.infrastructure.database.entity.DoctorEntity;
import pl.szczecin.infrastructure.database.repository.jpa.DoctorJpaRepository;
import pl.szczecin.infrastructure.database.repository.mapper.DoctorEntityMapper;
import pl.szczecin.util.EntityFixtures;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class DoctorRepositoryTest {

    @Mock
    private DoctorJpaRepository doctorJpaRepository;

    @Mock
    private DoctorEntityMapper doctorEntityMapper;

    @InjectMocks
    private DoctorRepository doctorRepository;


    @BeforeEach
    public void setUp() {
        Assertions.assertNotNull(doctorJpaRepository);
        Assertions.assertNotNull(doctorEntityMapper);
    }

    @Test
    @DisplayName("That method should return correct list of available doctors")
    void shouldReturnCorrectListOfAvailableDoctors() {
        // Given
        List<DoctorEntity> doctorEntities = List.of(
                EntityFixtures.someDoctorEntity2(),
                EntityFixtures.someDoctorEntity3()
        );
        List<Doctor> expectedDoctors = List.of(
                EntityFixtures.someDoctor1(),
                EntityFixtures.someDoctor2()
        );
        List<Doctor> notExpectedDoctors = List.of(
                EntityFixtures.someDoctor1(),
                EntityFixtures.someDoctor3()
        );

        Mockito.when(doctorJpaRepository.findAll()).thenReturn(doctorEntities);
        Mockito.when(doctorEntityMapper.mapFromEntity(Mockito.any(DoctorEntity.class)))
                .thenReturn(expectedDoctors.get(0))
                .thenReturn(expectedDoctors.get(1));

        // When
        List<Doctor> result = doctorRepository.findAvailableDoctors();

        // Then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(expectedDoctors, result);
        Assertions.assertNotEquals(notExpectedDoctors, result);

        Mockito.verify(doctorJpaRepository, Mockito.times(1)).findAll();
        Mockito.verify(doctorEntityMapper, Mockito.times(2))
                .mapFromEntity(Mockito.any(DoctorEntity.class));

        Mockito.verify(doctorEntityMapper, Mockito.never())
                .mapFromEntity(EntityFixtures.someDoctorEntity1().get());
    }

    @Test
    @DisplayName("That method should return correct doctor with given email")
    void shouldReturnCorrectDoctorWithGivenEmail() {
        // Given
        String doctorEmail = "anna.nowak@clinic.pl";
        Optional<DoctorEntity> doctorEntities = EntityFixtures.someDoctorEntity1();
        Doctor expectedDoctor = EntityFixtures.someDoctor1();
        Doctor notExpectedDoctors = EntityFixtures.someDoctor2();

        Mockito.when(doctorJpaRepository.findByEmail(doctorEmail)).thenReturn(doctorEntities);
        Mockito.when(doctorEntityMapper.mapFromEntity(Mockito.any(DoctorEntity.class)))
                .thenReturn(expectedDoctor);

        // When
        Doctor result = doctorRepository.findDoctorByEmail(doctorEmail).orElse(expectedDoctor);

        // Then
        Assertions.assertEquals(expectedDoctor, result);
        Assertions.assertNotEquals(notExpectedDoctors, result);

        Mockito.verify(doctorJpaRepository, Mockito.times(1)).findByEmail(Mockito.anyString());
        Mockito.verify(doctorEntityMapper, Mockito.times(1))
                .mapFromEntity(Mockito.any(DoctorEntity.class));

        Mockito.verify(doctorEntityMapper, Mockito.never())
                .mapFromEntity(EntityFixtures.someDoctorEntity3());
    }

    @Test
    @DisplayName("That method should return correct doctor with given surname")
    void shouldReturnCorrectDoctorWithGivenSurname() {
        // Given
        String doctorSurname = "Jankowska";
        Optional<DoctorEntity> doctorEntities = EntityFixtures.someDoctorEntity1();
        Doctor expectedDoctor = EntityFixtures.someDoctor1();
        Doctor notExpectedDoctors = EntityFixtures.someDoctor2();

        Mockito.when(doctorJpaRepository.findBySurname(doctorSurname)).thenReturn(doctorEntities);
        Mockito.when(doctorEntityMapper.mapFromEntity(Mockito.any(DoctorEntity.class)))
                .thenReturn(expectedDoctor);

        // When
        Doctor result = doctorRepository.findDoctorBySurname(doctorSurname).orElse(expectedDoctor);

        // Then
        Assertions.assertEquals(expectedDoctor, result);
        Assertions.assertNotEquals(notExpectedDoctors, result);

        Mockito.verify(doctorJpaRepository, Mockito.times(1)).findBySurname(Mockito.anyString());
        Mockito.verify(doctorEntityMapper, Mockito.times(1))
                .mapFromEntity(Mockito.any(DoctorEntity.class));

        Mockito.verify(doctorEntityMapper, Mockito.never())
                .mapFromEntity(EntityFixtures.someDoctorEntity3());
    }


}