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
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.infrastructure.database.entity.MedicalAppointmentDateEntity;
import pl.szczecin.infrastructure.database.repository.jpa.MedicalAppointmentDateJpaRepository;
import pl.szczecin.infrastructure.database.repository.mapper.MedicalAppointmentDateEntityMapper;
import pl.szczecin.util.EntityFixtures;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MedicalAppointmentDateRepositoryTest {

    @Mock
    private MedicalAppointmentDateJpaRepository medicalAppointmentDateJpaRepository;
    @Mock
    private MedicalAppointmentDateEntityMapper medicalAppointmentDateEntityMapper;

    @InjectMocks
    private MedicalAppointmentDateRepository medicalAppointmentDateRepository;


    @BeforeEach
    public void setUp() {
        Assertions.assertNotNull(medicalAppointmentDateJpaRepository);
        Assertions.assertNotNull(medicalAppointmentDateEntityMapper);
    }

    @Test
    @DisplayName("That method should return correct list of available dates with given doctor email")
    void shouldReturnCorrectListOfAvailableDatesWithGivenDoctorEmail() {
        // Given
        String doctorEmail = "doctor.doctor@clinic.pl";
        List<MedicalAppointmentDateEntity> someMedicalAppointmentDateEntities = List.of(
                EntityFixtures.someMedicalAppointmentDateEntity1(),
                EntityFixtures.someMedicalAppointmentDateEntity2()
        );
        List<MedicalAppointmentDate> expectedMedicalAppointmentDates = List.of(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate2()
        );
        List<MedicalAppointmentDate> notExpectedMedicalAppointments = List.of(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate3()
        );

        Mockito.when(medicalAppointmentDateJpaRepository.findAvailableDatesByDoctorEmail(doctorEmail))
                .thenReturn(someMedicalAppointmentDateEntities);
        Mockito.when(medicalAppointmentDateEntityMapper.mapFromEntity(Mockito.any(MedicalAppointmentDateEntity.class)))
                .thenReturn(expectedMedicalAppointmentDates.get(0))
                .thenReturn(expectedMedicalAppointmentDates.get(1));

        // When
        List<MedicalAppointmentDate> result = medicalAppointmentDateRepository
                .findAvailableDatesByDoctorEmail(doctorEmail);

        // Then
        org.assertj.core.api.Assertions.assertThat(result).isNotNull();
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(expectedMedicalAppointmentDates, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointments, result);

        Mockito.verify(medicalAppointmentDateJpaRepository, Mockito.times(1))
                .findAvailableDatesByDoctorEmail(Mockito.anyString());
        Mockito.verify(medicalAppointmentDateEntityMapper, Mockito.times(2))
                .mapFromEntity(Mockito.any(MedicalAppointmentDateEntity.class));
    }


    @Test
    @DisplayName("That method should return correct list of future dates with given doctor email")
    void shouldReturnCorrectListOfFutureDatesWithGivenDoctorEmail() {
        // Given
        String doctorEmail = "doctor.doctor@clinic.pl";
        List<MedicalAppointmentDateEntity> someMedicalAppointmentDateEntities = List.of(
                EntityFixtures.someMedicalAppointmentDateEntity1(),
                EntityFixtures.someMedicalAppointmentDateEntity2()
        );
        List<MedicalAppointmentDate> expectedMedicalAppointmentDates = List.of(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate2()
        );
        List<MedicalAppointmentDate> notExpectedMedicalAppointments = List.of(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate3()
        );

        Mockito.when(medicalAppointmentDateJpaRepository.findAllFutureDatesByDoctorEmail(doctorEmail))
                .thenReturn(someMedicalAppointmentDateEntities);
        Mockito.when(medicalAppointmentDateEntityMapper.mapFromEntity(Mockito.any(MedicalAppointmentDateEntity.class)))
                .thenReturn(expectedMedicalAppointmentDates.get(0))
                .thenReturn(expectedMedicalAppointmentDates.get(1));

        // When
        List<MedicalAppointmentDate> result = medicalAppointmentDateRepository
                .findAllFutureDatesByDoctorEmail(doctorEmail);

        // Then
        org.assertj.core.api.Assertions.assertThat(result).isNotNull();
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(expectedMedicalAppointmentDates, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointments, result);

        Mockito.verify(medicalAppointmentDateJpaRepository, Mockito.times(1))
                .findAllFutureDatesByDoctorEmail(Mockito.anyString());
        Mockito.verify(medicalAppointmentDateEntityMapper, Mockito.times(2))
                .mapFromEntity(Mockito.any(MedicalAppointmentDateEntity.class));
    }

    @Test
    @DisplayName("That method should return correct list of history dates with given doctor email")
    void shouldReturnCorrectListOfHistoryDatesWithGivenDoctorEmail() {
        // Given
        String doctorEmail = "doctor.doctor@clinic.pl";
        List<MedicalAppointmentDateEntity> someMedicalAppointmentDateEntities = List.of(
                EntityFixtures.someMedicalAppointmentDateEntity1(),
                EntityFixtures.someMedicalAppointmentDateEntity2()
        );
        List<MedicalAppointmentDate> expectedMedicalAppointmentDates = List.of(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate2()
        );
        List<MedicalAppointmentDate> notExpectedMedicalAppointments = List.of(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate3()
        );

        Mockito.when(medicalAppointmentDateJpaRepository.findAllHistoryDatesByDoctorEmail(doctorEmail))
                .thenReturn(someMedicalAppointmentDateEntities);
        Mockito.when(medicalAppointmentDateEntityMapper.mapFromEntity(Mockito.any(MedicalAppointmentDateEntity.class)))
                .thenReturn(expectedMedicalAppointmentDates.get(0))
                .thenReturn(expectedMedicalAppointmentDates.get(1));

        // When
        List<MedicalAppointmentDate> result = medicalAppointmentDateRepository
                .findAllHistoryDatesByDoctorEmail(doctorEmail);

        // Then
        org.assertj.core.api.Assertions.assertThat(result).isNotNull();
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(expectedMedicalAppointmentDates, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointments, result);

        Mockito.verify(medicalAppointmentDateJpaRepository, Mockito.times(1))
                .findAllHistoryDatesByDoctorEmail(Mockito.anyString());
        Mockito.verify(medicalAppointmentDateEntityMapper, Mockito.times(2))
                .mapFromEntity(Mockito.any(MedicalAppointmentDateEntity.class));
    }

    @Test
    @DisplayName("That method should return correct Optional of Medical Appointment Date with given date and doctor email")
    void shouldReturnCorrectMedicalAppointmentDateWithGivenDateAndDoctorEmail() {
        // Given
        String doctorEmail = "doctor.doctor@clinic.pl";
        OffsetDateTime dateTime = OffsetDateTime.of(2023, 11, 15,
                10, 0, 0, 0, ZoneOffset.UTC);
        MedicalAppointmentDateEntity someMedicalAppointmentDateEntity =
                EntityFixtures.someMedicalAppointmentDateEntity1();
        MedicalAppointmentDate expectedMedicalAppointmentDate = EntityFixtures.someMedicalAppointmentDate1();
        MedicalAppointmentDate notExpectedMedicalAppointmentDate = EntityFixtures.someMedicalAppointmentDate2();

        Mockito.when(medicalAppointmentDateJpaRepository.findByDateTimeAndDoctor(dateTime, doctorEmail))
                .thenReturn(Optional.of(someMedicalAppointmentDateEntity));
        Mockito.when(medicalAppointmentDateEntityMapper.mapFromEntity(Mockito.any(MedicalAppointmentDateEntity.class)))
                .thenReturn(expectedMedicalAppointmentDate);

        // When
        Optional<MedicalAppointmentDate> result = medicalAppointmentDateRepository
                .findMedicalAppointmentDateByDateAndDoctor(dateTime, doctorEmail);

        // Then
        org.assertj.core.api.Assertions.assertThat(result).isPresent();
        Assertions.assertEquals(expectedMedicalAppointmentDate, result.get());
        Assertions.assertNotEquals(notExpectedMedicalAppointmentDate, result.get());

        Mockito.verify(medicalAppointmentDateJpaRepository, Mockito.times(1))
                .findByDateTimeAndDoctor(Mockito.any(OffsetDateTime.class), Mockito.anyString());
        Mockito.verify(medicalAppointmentDateEntityMapper, Mockito.times(1))
                .mapFromEntity(Mockito.any(MedicalAppointmentDateEntity.class));
    }

    @Test
    @DisplayName("That method should correctly save Medical Appointment Date with Medical Appointment Date")
    void shouldCorrectlySaveMedicalAppointmentDateWithMedicalAppointmentDate() {
        // Given
        MedicalAppointmentDateEntity someMedicalAppointmentDateEntity =
                EntityFixtures.someMedicalAppointmentDateEntity1();
        MedicalAppointmentDate expectedMedicalAppointmentDate = EntityFixtures.someMedicalAppointmentDate1();
        MedicalAppointmentDate notExpectedMedicalAppointmentDate = EntityFixtures.someMedicalAppointmentDate2();

        Mockito.when(medicalAppointmentDateEntityMapper.mapToEntity(Mockito.any(MedicalAppointmentDate.class)))
                .thenReturn(someMedicalAppointmentDateEntity);
        Mockito.when(medicalAppointmentDateJpaRepository.save(Mockito.any(MedicalAppointmentDateEntity.class)))
                .thenReturn(someMedicalAppointmentDateEntity);
        Mockito.when(medicalAppointmentDateEntityMapper.mapFromEntity(Mockito.any(MedicalAppointmentDateEntity.class)))
                .thenReturn(expectedMedicalAppointmentDate);

        // When
        MedicalAppointmentDate result = medicalAppointmentDateRepository
                .saveMedicalAppointmentDate(expectedMedicalAppointmentDate);

        // Then
        Assertions.assertEquals(expectedMedicalAppointmentDate, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointmentDate, result);

        Mockito.verify(medicalAppointmentDateEntityMapper, Mockito.times(1))
                .mapToEntity(Mockito.any(MedicalAppointmentDate.class));
        Mockito.verify(medicalAppointmentDateJpaRepository, Mockito.times(1))
                .save(Mockito.any(MedicalAppointmentDateEntity.class));
        Mockito.verify(medicalAppointmentDateEntityMapper, Mockito.times(1))
                .mapFromEntity(Mockito.any(MedicalAppointmentDateEntity.class));
    }

}
