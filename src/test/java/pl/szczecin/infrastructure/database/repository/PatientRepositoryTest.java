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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pl.szczecin.domain.Patient;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.infrastructure.database.entity.MedicalAppointmentEntity;
import pl.szczecin.infrastructure.database.entity.PatientEntity;
import pl.szczecin.infrastructure.database.repository.jpa.PatientJpaRepository;
import pl.szczecin.infrastructure.database.repository.mapper.MedicalAppointmentEntityMapper;
import pl.szczecin.infrastructure.database.repository.mapper.PatientEntityMapper;
import pl.szczecin.util.EntityFixtures;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PatientRepositoryTest {

    @Mock
    private PatientJpaRepository patientJpaRepository;
    @Mock
    private PatientEntityMapper patientEntityMapper;
    @Mock
    private MedicalAppointmentEntityMapper medicalAppointmentEntityMapper;

    @InjectMocks
    private PatientRepository patientRepository;


    @BeforeEach
    public void setUp() {
        Assertions.assertNotNull(patientJpaRepository);
        Assertions.assertNotNull(patientEntityMapper);
        Assertions.assertNotNull(medicalAppointmentEntityMapper);
    }


    @Test
    @DisplayName("That method should correct save patient")
    void shouldCorrectSavePatient() {
        //given
        PatientEntity somePatientEntity = EntityFixtures.somePatientEntity1();
        Patient expectedPatient = EntityFixtures.somePatient1();
        Patient notExpectedPatient = EntityFixtures.somePatient2();

        Mockito.when(patientEntityMapper.mapToEntity(Mockito.any(Patient.class))).thenReturn(somePatientEntity);
        Mockito.when(patientJpaRepository.saveAndFlush(Mockito.any(PatientEntity.class))).thenReturn(somePatientEntity);
        Mockito.when(patientEntityMapper.mapFromEntity(Mockito.any(PatientEntity.class))).thenReturn(expectedPatient);

        //when
        Patient result = patientRepository.savePatient(expectedPatient);

        //then
        Assertions.assertEquals(expectedPatient, result);
        Assertions.assertNotEquals(notExpectedPatient, result);

        Mockito.verify(patientEntityMapper, Mockito.times(1))
                .mapToEntity(Mockito.any(Patient.class));
        Mockito.verify(patientEntityMapper, Mockito.times(1))
                .mapFromEntity(Mockito.any(PatientEntity.class));
        Mockito.verify(patientJpaRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(PatientEntity.class));
        Mockito.verify(patientEntityMapper, Mockito.never())
                .mapToEntity(notExpectedPatient);

        Mockito.verifyNoInteractions(medicalAppointmentEntityMapper);
    }


    @Test
    @DisplayName("That method should return correct list of available patients")
    void shouldReturnCorrectListOfAvailablePatients() {
        // Given
        List<PatientEntity> patientEntities = List.of(
                EntityFixtures.somePatientEntity1(),
                EntityFixtures.somePatientEntity2()
        );
        List<Patient> expectedPatients = List.of(
                EntityFixtures.somePatient1(),
                EntityFixtures.somePatient2()
        );
        List<Patient> notExpectedPatients = List.of(
                EntityFixtures.somePatient1(),
                EntityFixtures.somePatient3()
        );

        // Data to pagination and sorting
        int pageNumber = 1;
        int pageSize= 2;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("surname").ascending()
                .and(Sort.by("name")).ascending());
        PageImpl<PatientEntity> pageImplementation = new PageImpl<>(patientEntities);

        Mockito.when(patientJpaRepository.findAll(pageable))
                .thenReturn(pageImplementation);
        Mockito.when(patientEntityMapper.mapFromEntity(Mockito.any(PatientEntity.class)))
                .thenReturn(expectedPatients.get(0))
                .thenReturn(expectedPatients.get(1));

        // When
        List<Patient> result = patientRepository.findAvailablePatients(pageNumber,pageSize);

        // Then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(expectedPatients, result);
        Assertions.assertNotEquals(notExpectedPatients, result);

        Mockito.verify(patientJpaRepository, Mockito.times(1))
                .findAll(Mockito.any(Pageable.class));
        Mockito.verify(patientEntityMapper, Mockito.times(2))
                .mapFromEntity(Mockito.any(PatientEntity.class));

        Mockito.verifyNoInteractions(medicalAppointmentEntityMapper);
    }


    @Test
    @DisplayName("That method should return correct patient with given email")
    void shouldReturnCorrectPatientWithGivenEmail() {
        // Given
        String patientEmail = "anna.nowak@clinic.pl";
        Optional<PatientEntity> patientEntities = EntityFixtures.somePatientEntity3();
        Patient expectedPatient = EntityFixtures.somePatient1();
        Patient notExpectedPatients = EntityFixtures.somePatient2();

        Mockito.when(patientJpaRepository.findOptionalByEmail(patientEmail)).thenReturn(patientEntities);
        Mockito.when(patientEntityMapper.mapFromEntity(Mockito.any(PatientEntity.class)))
                .thenReturn(expectedPatient);

        // When
        Patient result = patientRepository.findPatientByEmail(patientEmail).orElse(expectedPatient);

        // Then
        Assertions.assertEquals(expectedPatient, result);
        Assertions.assertNotEquals(notExpectedPatients, result);

        Mockito.verify(patientJpaRepository, Mockito.times(1))
                .findOptionalByEmail(Mockito.anyString());
        Mockito.verify(patientEntityMapper, Mockito.times(1))
                .mapFromEntity(Mockito.any(PatientEntity.class));

        Mockito.verifyNoInteractions(medicalAppointmentEntityMapper);
    }


    @Test
    @DisplayName("That method should return correct patient history with given email")
    void shouldReturnCorrectPatientHistoryWithGivenEmail() {
        // Given
        String patientEmail = "test@example.com";
        PatientEntity somePatientEntity = EntityFixtures.somePatientEntity1();
        List<PatientHistory.MedicalAppointment> somePastAppointments = List.of(
                EntityFixtures.someMedicalAppointmentFromPatientHistory1(),
                EntityFixtures.someMedicalAppointmentFromPatientHistory2()
        );

        PatientHistory expectedPatientHistory = EntityFixtures.somePatientHistory()
                .withMedicalAppointments(somePastAppointments);

        Mockito.when(patientJpaRepository.findByEmail(Mockito.anyString())).thenReturn(somePatientEntity);
        Mockito.when(medicalAppointmentEntityMapper
                        .mapFromMedicalAppointmentEntityToPatientHistoryMedicalAppointment(
                                Mockito.any(MedicalAppointmentEntity.class)))
                .thenReturn(somePastAppointments.get(0));
        Mockito.when(patientEntityMapper.mapFromEntity(Mockito.any(PatientEntity.class), Mockito.anyString()))
                .thenReturn(expectedPatientHistory);

        // When
        PatientHistory result = patientRepository.findPatientHistoryByEmail(patientEmail)
                .withMedicalAppointments(somePastAppointments);

        // Then
        Assertions.assertEquals(expectedPatientHistory, result);

        Mockito.verify(patientJpaRepository, Mockito.times(1))
                .findByEmail(Mockito.anyString());
        Mockito.verify(patientEntityMapper, Mockito.times(1))
                .mapFromEntity(Mockito.any(PatientEntity.class), Mockito.anyString());
        Mockito.verify(medicalAppointmentEntityMapper, Mockito.times(1))
                .mapFromMedicalAppointmentEntityToPatientHistoryMedicalAppointment(
                        Mockito.any(MedicalAppointmentEntity.class));

    }

    @Test
    @DisplayName("That method should return correct patient schedule with given email")
    void shouldReturnCorrectPatientScheduleWithGivenEmail() {
        // Given
        String patientEmail = "test@example.com";
        PatientEntity somePatientEntity = EntityFixtures.somePatientEntity1();
        List<PatientHistory.MedicalAppointment> someFutureAppointments = List.of(
                EntityFixtures.someMedicalAppointmentFromPatientHistory1(),
                EntityFixtures.someMedicalAppointmentFromPatientHistory2()
        );

        PatientHistory expectedPatientHistory = EntityFixtures.somePatientHistory()
                .withMedicalAppointments(someFutureAppointments);

        Mockito.when(patientJpaRepository.findByEmail(Mockito.anyString())).thenReturn(somePatientEntity);
        Mockito.when(patientEntityMapper.mapFromEntity(Mockito.any(PatientEntity.class), Mockito.anyString()))
                .thenReturn(expectedPatientHistory);

        // When
        PatientHistory result = patientRepository.findPatientScheduleByEmail(patientEmail)
                .withMedicalAppointments(someFutureAppointments);

        // Then
        Assertions.assertEquals(expectedPatientHistory, result);

        Mockito.verify(patientJpaRepository, Mockito.times(1))
                .findByEmail(Mockito.anyString());
        Mockito.verify(patientEntityMapper, Mockito.times(1))
                .mapFromEntity(Mockito.any(PatientEntity.class), Mockito.anyString());

        Mockito.verifyNoInteractions(medicalAppointmentEntityMapper);

    }

    @Test
    @DisplayName("That method should return correct patient appointments to cancel with given email")
    void shouldReturnCorrectPatientAppointmentsToCancelWithGivenEmail() {
        // Given
        String patientEmail = "test@example.com";
        PatientEntity somePatientEntity = EntityFixtures.somePatientEntity1();
        List<PatientHistory.MedicalAppointment> someFutureAppointments = List.of(
                EntityFixtures.someMedicalAppointmentFromPatientHistory1(),
                EntityFixtures.someMedicalAppointmentFromPatientHistory2()
        );

        PatientHistory expectedPatientHistory = EntityFixtures.somePatientHistory()
                .withMedicalAppointments(someFutureAppointments);

        Mockito.when(patientJpaRepository.findByEmail(Mockito.anyString())).thenReturn(somePatientEntity);
        Mockito.when(patientEntityMapper.mapFromEntity(Mockito.any(PatientEntity.class), Mockito.anyString()))
                .thenReturn(expectedPatientHistory);

        // When
        PatientHistory result = patientRepository.findPatientAppointmentsToCancelByEmail(patientEmail)
                .withMedicalAppointments(someFutureAppointments);

        // Then
        Assertions.assertEquals(expectedPatientHistory, result);

        Mockito.verify(patientJpaRepository, Mockito.times(1))
                .findByEmail(Mockito.anyString());
        Mockito.verify(patientEntityMapper, Mockito.times(1))
                .mapFromEntity(Mockito.any(PatientEntity.class), Mockito.anyString());

        Mockito.verifyNoInteractions(medicalAppointmentEntityMapper);

    }

}