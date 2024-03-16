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
import pl.szczecin.domain.MedicalAppointment;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.infrastructure.database.entity.MedicalAppointmentEntity;
import pl.szczecin.infrastructure.database.repository.jpa.MedicalAppointmentJpaRepository;
import pl.szczecin.infrastructure.database.repository.mapper.MedicalAppointmentEntityMapper;
import pl.szczecin.util.EntityFixtures;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MedicalAppointmentRepositoryTest {

    @Mock
    private MedicalAppointmentJpaRepository medicalAppointmentJpaRepository;
    @Mock
    private MedicalAppointmentEntityMapper medicalAppointmentEntityMapper;

    @InjectMocks
    private MedicalAppointmentRepository medicalAppointmentRepository;


    @BeforeEach
    public void setUp() {
        Assertions.assertNotNull(medicalAppointmentJpaRepository);
        Assertions.assertNotNull(medicalAppointmentEntityMapper);
    }


    @Test
    @DisplayName("That method should return correct list of available medical appointments")
    void shouldReturnCorrectListOfAvailableMedicalAppointments() {
        // Given
        List<MedicalAppointmentEntity> someMedicalAppointmentEntities = List.of(
                EntityFixtures.someMedicalAppointmentEntity1(),
                EntityFixtures.someMedicalAppointmentEntity2()
        );
        List<MedicalAppointment> expectedMedicalAppointments = List.of(
                EntityFixtures.someMedicalAppointment1(),
                EntityFixtures.someMedicalAppointment2()
        );
        List<MedicalAppointment> notExpectedMedicalAppointments = List.of(
                EntityFixtures.someMedicalAppointment1(),
                EntityFixtures.someMedicalAppointment3()
        );

        Mockito.when(medicalAppointmentJpaRepository.findAll()).thenReturn(someMedicalAppointmentEntities);
        Mockito.when(medicalAppointmentEntityMapper.mapFromEntity(Mockito.any(MedicalAppointmentEntity.class)))
                .thenReturn(expectedMedicalAppointments.get(0))
                .thenReturn(expectedMedicalAppointments.get(1));

        // When
        List<MedicalAppointment> result = medicalAppointmentRepository.findAllMedicalAppointment();

        // Then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(expectedMedicalAppointments, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointments, result);

        Mockito.verify(medicalAppointmentJpaRepository, Mockito.times(1)).findAll();
        Mockito.verify(medicalAppointmentEntityMapper, Mockito.times(2))
                .mapFromEntity(Mockito.any(MedicalAppointmentEntity.class));

    }

    @Test
    @DisplayName("That method should return correct list of available medical appointments with given List of Medical Appointment Date IDs")
    void shouldReturnCorrectListOfAvailableMedicalAppointmentsWithGivenMADIds() {
        // Given
        List<Integer> someMedicalAppointmentDateIds = Arrays.asList(1, 2, 3);
        List<MedicalAppointmentEntity> someMedicalAppointmentEntities = List.of(
                EntityFixtures.someMedicalAppointmentEntity1(),
                EntityFixtures.someMedicalAppointmentEntity2()
        );
        List<MedicalAppointment> expectedMedicalAppointments = List.of(
                EntityFixtures.someMedicalAppointment1(),
                EntityFixtures.someMedicalAppointment2()
        );
        List<MedicalAppointment> notExpectedMedicalAppointments = List.of(
                EntityFixtures.someMedicalAppointment1(),
                EntityFixtures.someMedicalAppointment3()
        );

        Mockito.when(medicalAppointmentJpaRepository.findAllMedicalAppointmentByMADateID(someMedicalAppointmentDateIds))
                .thenReturn(someMedicalAppointmentEntities);
        Mockito.when(medicalAppointmentEntityMapper.mapFromEntity(Mockito.any(MedicalAppointmentEntity.class)))
                .thenReturn(expectedMedicalAppointments.get(0))
                .thenReturn(expectedMedicalAppointments.get(1));

        // When
        List<MedicalAppointment> result = medicalAppointmentRepository
                .findAllMedicalAppointmentByMADateID(someMedicalAppointmentDateIds);

        // Then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(expectedMedicalAppointments, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointments, result);

        Mockito.verify(medicalAppointmentJpaRepository, Mockito.times(1))
                .findAllMedicalAppointmentByMADateID(someMedicalAppointmentDateIds);
        Mockito.verify(medicalAppointmentJpaRepository, Mockito.never())
                .findAllMedicalAppointmentByMADateID(List.of(3, 4, 5));
        Mockito.verify(medicalAppointmentEntityMapper, Mockito.times(2))
                .mapFromEntity(Mockito.any(MedicalAppointmentEntity.class));

    }

    @Test
    @DisplayName("That method should correctly make medical appointment with given Medical Appointment")
    void shouldCorrectlyMakeMedicalAppointmentWithGivenMedicalAppointment() {
        // Given
        MedicalAppointmentEntity someMedicalAppointmentEntity = EntityFixtures.someMedicalAppointmentEntity1();

        MedicalAppointment expectedMedicalAppointment = EntityFixtures.someMedicalAppointment1();
        MedicalAppointment notExpectedMedicalAppointment = EntityFixtures.someMedicalAppointment3();

        Mockito.when(medicalAppointmentEntityMapper.mapToEntity(Mockito.any(MedicalAppointment.class)))
                .thenReturn(someMedicalAppointmentEntity);
        Mockito.when(medicalAppointmentJpaRepository.saveAndFlush(Mockito.any(MedicalAppointmentEntity.class)))
                .thenReturn(someMedicalAppointmentEntity);
        Mockito.when(medicalAppointmentEntityMapper.mapFromEntity(Mockito.any(MedicalAppointmentEntity.class)))
                .thenReturn(expectedMedicalAppointment);

        // When
        MedicalAppointment result = medicalAppointmentRepository.makeAppointment(expectedMedicalAppointment);

        // Then
        Assertions.assertEquals(expectedMedicalAppointment, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointment, result);

        Mockito.verify(medicalAppointmentEntityMapper, Mockito.times(1))
                .mapToEntity(Mockito.any(MedicalAppointment.class));
        Mockito.verify(medicalAppointmentJpaRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(MedicalAppointmentEntity.class));
        Mockito.verify(medicalAppointmentEntityMapper, Mockito.times(1))
                .mapFromEntity(Mockito.any(MedicalAppointmentEntity.class));

    }

    @Test
    @DisplayName("That method should correctly cancel medical appointment with given Medical Appointment Date ID")
    void shouldCorrectlyCancelMedicalAppointmentWithGivenMedicalAppointmentDateID() {
        // Given
        MedicalAppointmentEntity someMedicalAppointmentEntity = EntityFixtures.someMedicalAppointmentEntity1();

        MedicalAppointment expectedMedicalAppointment = EntityFixtures.someMedicalAppointment1();
        MedicalAppointment notExpectedMedicalAppointment = EntityFixtures.someMedicalAppointment3();

        Mockito.when(medicalAppointmentJpaRepository.findByMedicalAppointmentDateId(Mockito.anyInt()))
                .thenReturn(someMedicalAppointmentEntity);
        Mockito.when(medicalAppointmentJpaRepository.existsById(someMedicalAppointmentEntity.getMedicalAppointmentId()))
                .thenReturn(true);
        Mockito.when(medicalAppointmentEntityMapper.mapFromEntity(Mockito.any(MedicalAppointmentEntity.class)))
                .thenReturn(expectedMedicalAppointment);

        // When
        MedicalAppointment result = medicalAppointmentRepository.cancelMedicalAppointment(1);

        // Then
        Assertions.assertEquals(expectedMedicalAppointment, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointment, result);

        Mockito.verify(medicalAppointmentJpaRepository, Mockito.times(2))
                .findByMedicalAppointmentDateId(Mockito.anyInt());
        Mockito.verify(medicalAppointmentJpaRepository, Mockito.times(1))
                .existsById(someMedicalAppointmentEntity.getMedicalAppointmentId());
        Mockito.verify(medicalAppointmentJpaRepository, Mockito.times(1))
                .deleteById(someMedicalAppointmentEntity.getMedicalAppointmentId());
        Mockito.verify(medicalAppointmentEntityMapper, Mockito.times(1))
                .mapFromEntity(Mockito.any(MedicalAppointmentEntity.class));

    }

    @Test
    @DisplayName("That method should correctly add note to medical appointment with given request")
    void shouldCorrectlyAddNoteToMedicalAppointmentWithGivenRequest() {
        // Given
        MedicalAppointmentRequest someMedicalAppointmentRequest = EntityFixtures.someMedicalAppointmentRequest();
        MedicalAppointmentEntity someMedicalAppointmentEntity = EntityFixtures.someMedicalAppointmentEntity1();

        MedicalAppointment expectedMedicalAppointment = EntityFixtures.someMedicalAppointment1();
        MedicalAppointment notExpectedMedicalAppointment = EntityFixtures.someMedicalAppointment3();

        Mockito.when(medicalAppointmentJpaRepository.findByDataFromRequest(
                        Mockito.any(OffsetDateTime.class),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyString()))
                .thenReturn(someMedicalAppointmentEntity);
        Mockito.when(medicalAppointmentJpaRepository.saveAndFlush(Mockito.any(MedicalAppointmentEntity.class)))
                .thenReturn(someMedicalAppointmentEntity);
        Mockito.when(medicalAppointmentEntityMapper.mapFromEntity(Mockito.any(MedicalAppointmentEntity.class)))
                .thenReturn(expectedMedicalAppointment);

        // When
        Optional<MedicalAppointment> result = medicalAppointmentRepository
                .addNoteToMedicalAppointment(someMedicalAppointmentRequest);

        // Then
        org.assertj.core.api.Assertions.assertThat(result).isPresent();
        Assertions.assertEquals(expectedMedicalAppointment, result.get());
        Assertions.assertNotEquals(notExpectedMedicalAppointment, result.get());

        Mockito.verify(medicalAppointmentJpaRepository, Mockito.times(1))
                .findByDataFromRequest(
                        Mockito.any(OffsetDateTime.class),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyString());
        Mockito.verify(medicalAppointmentJpaRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(MedicalAppointmentEntity.class));
        Mockito.verify(medicalAppointmentEntityMapper, Mockito.times(1))
                .mapFromEntity(Mockito.any(MedicalAppointmentEntity.class));

    }

    @Test
    @DisplayName("That method should return Optional.empty() when medical appointment does not exist")
    void shouldReturnOptionalEmptyWhenMedicalAppointmentDoesNotExist() {
        // Given
        MedicalAppointmentRequest someMedicalAppointmentRequest = EntityFixtures.someMedicalAppointmentRequest();

        Mockito.when(medicalAppointmentJpaRepository.findByDataFromRequest(
                        Mockito.any(OffsetDateTime.class),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyString()))
                .thenReturn(null);

        // When
        Optional<MedicalAppointment> result = medicalAppointmentRepository
                .addNoteToMedicalAppointment(someMedicalAppointmentRequest);

        // Then
        org.assertj.core.api.Assertions.assertThat(result).isEmpty();

        Mockito.verify(medicalAppointmentJpaRepository, Mockito.times(1))
                .findByDataFromRequest(
                        Mockito.any(OffsetDateTime.class),
                        Mockito.anyString(),
                        Mockito.anyString(),
                        Mockito.anyString());
        Mockito.verify(medicalAppointmentJpaRepository, Mockito.never())
                .saveAndFlush(Mockito.any(MedicalAppointmentEntity.class));
        Mockito.verify(medicalAppointmentEntityMapper, Mockito.never())
                .mapFromEntity(Mockito.any(MedicalAppointmentEntity.class));

        Mockito.verifyNoInteractions(medicalAppointmentEntityMapper);

    }

}
