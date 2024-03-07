package pl.szczecin.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.szczecin.business.dao.MedicalAppointmentDateDAO;
import pl.szczecin.domain.MedicalAppointmentDate;
import pl.szczecin.domain.exception.NotFoundException;
import pl.szczecin.util.EntityFixtures;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MedicalAppointmentDateServiceTest {

    @Mock
    private MedicalAppointmentDateDAO medicalAppointmentDateDAO;

    @InjectMocks
    private MedicalAppointmentDateService medicalAppointmentDateService;


    @BeforeEach
    public void setUp() {
        System.out.println("checking for nulls");
        Assertions.assertNotNull(medicalAppointmentDateDAO);
    }


    @Test
    @DisplayName("That method should return all available medical appointment dates with given doctor email")
    void shouldReturnAllAvailableDatesWithGivenDoctorEmail() {
        // given
        String doctorEmail = "example@clinic.com";
        List<MedicalAppointmentDate> expectedMedicalAppointmentDates = Arrays.asList(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate2()
        );
        List<MedicalAppointmentDate> notExpectedMedicalAppointmentDates = Arrays.asList(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate3()
        );
        Mockito.when(medicalAppointmentDateDAO.findAvailableDatesByDoctorEmail(doctorEmail))
                .thenReturn(expectedMedicalAppointmentDates);

        // when
        List<MedicalAppointmentDate> result = medicalAppointmentDateService.getAvailableDatesByDoctorEmail(doctorEmail);

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(expectedMedicalAppointmentDates, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointmentDates, result);

        Mockito.verify(medicalAppointmentDateDAO, Mockito.times(1))
                .findAvailableDatesByDoctorEmail(Mockito.anyString());
        Mockito.verify(medicalAppointmentDateDAO, Mockito.never())
                .findAvailableDatesByDoctorEmail("other.email@clinic.pl");

    }


    @Test
    @DisplayName("That method should return all history medical appointment dates with given doctor email")
    void shouldReturnAllHistoryDatesWithGivenDoctorEmail() {
        // given
        String doctorEmail = "example@clinic.com";
        List<MedicalAppointmentDate> expectedMedicalAppointmentDates = Arrays.asList(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate2()
        );
        List<MedicalAppointmentDate> notExpectedMedicalAppointmentDates = Arrays.asList(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate3()
        );
        Mockito.when(medicalAppointmentDateDAO.findAllHistoryDatesByDoctorEmail(doctorEmail))
                .thenReturn(expectedMedicalAppointmentDates);

        // when
        List<MedicalAppointmentDate> result = medicalAppointmentDateService.getAllHistoryDatesByDoctorEmail(doctorEmail);

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(expectedMedicalAppointmentDates, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointmentDates, result);

        Mockito.verify(medicalAppointmentDateDAO, Mockito.times(1))
                .findAllHistoryDatesByDoctorEmail(Mockito.anyString());
        Mockito.verify(medicalAppointmentDateDAO, Mockito.never())
                .findAllHistoryDatesByDoctorEmail("other.email@clinic.pl");

    }

    @Test
    @DisplayName("That method should return all future medical appointment dates with given doctor email")
    void shouldReturnAllFutureDatesWithGivenDoctorEmail() {
        // given
        String doctorEmail = "example@clinic.com";
        List<MedicalAppointmentDate> expectedMedicalAppointmentDates = Arrays.asList(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate2()
        );
        List<MedicalAppointmentDate> notExpectedMedicalAppointmentDates = Arrays.asList(
                EntityFixtures.someMedicalAppointmentDate1(),
                EntityFixtures.someMedicalAppointmentDate3()
        );

        Mockito.when(medicalAppointmentDateDAO.findAllFutureDatesByDoctorEmail(doctorEmail))
                .thenReturn(expectedMedicalAppointmentDates);

        // when
        List<MedicalAppointmentDate> result = medicalAppointmentDateService.getAllFutureDatesByDoctorEmail(doctorEmail);

        // then
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(expectedMedicalAppointmentDates, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointmentDates, result);

        Mockito.verify(medicalAppointmentDateDAO, Mockito.times(1))
                .findAllFutureDatesByDoctorEmail(Mockito.anyString());
        Mockito.verify(medicalAppointmentDateDAO, Mockito.never())
                .findAllFutureDatesByDoctorEmail("other.email@clinic.pl");

    }

    @Test
    @DisplayName("That method should return correct medical appointment date with given date and doctor email")
    void shouldReturnCorrectMedicalAppointmentWithGivenDateAndDoctorEmail() {
        // given
        String doctorEmail = "example@clinic.com";
        OffsetDateTime dateTime = OffsetDateTime.of(2023, 11, 15,
                10, 0, 0, 0, ZoneOffset.UTC);
        MedicalAppointmentDate expectedMedicalAppointmentDate = EntityFixtures.someMedicalAppointmentDate1();

        Mockito.when(medicalAppointmentDateDAO.findMedicalAppointmentDateByDateAndDoctor(dateTime, doctorEmail))
                .thenReturn(Optional.ofNullable(expectedMedicalAppointmentDate));

        // when
        MedicalAppointmentDate result = medicalAppointmentDateService
                .findMedicalAppointmentDateByDateAndDoctor(dateTime, doctorEmail);

        // then
        Assertions.assertEquals(expectedMedicalAppointmentDate, result);

        Mockito.verify(medicalAppointmentDateDAO, Mockito.times(1))
                .findMedicalAppointmentDateByDateAndDoctor(
                        Mockito.any(OffsetDateTime.class),
                        Mockito.anyString());
        Mockito.verify(medicalAppointmentDateDAO, Mockito.never())
                .findMedicalAppointmentDateByDateAndDoctor(dateTime, "other.email@clinic.pl");

    }


    @Test
    @DisplayName("That method should return empty medical appointment date and throw NotFoundException with given date and doctor email")
    public void shouldReturnEmptyMedicalAppointmentDateAndThrowNotFoundException() {
        // given
        String doctorEmail = "doctor@test.com";
        OffsetDateTime dateTime = OffsetDateTime.of(2023, 11, 15,
                10, 0, 0, 0, ZoneOffset.UTC);
        Optional<MedicalAppointmentDate> expectedMedicalAppointmentDate = Optional.empty();

        Mockito.when(medicalAppointmentDateDAO.findMedicalAppointmentDateByDateAndDoctor(dateTime, doctorEmail))
                .thenReturn(expectedMedicalAppointmentDate);

        // when, then
        NotFoundException exception =
                Assertions.assertThrows(NotFoundException.class, () -> medicalAppointmentDateService
                        .findMedicalAppointmentDateByDateAndDoctor(dateTime, doctorEmail));
        Assertions.assertEquals((
                        "Could not find medicalAppointmentDate by date and Doctor: [%s]"
                                .formatted(expectedMedicalAppointmentDate)),
                exception.getMessage());
    }


    @Test
    @DisplayName("That method should correctly save medical appointment with given medical appointment date")
    void shouldCorrectlySaveMedicalAppointmentWithGivenMedicalAppointmentDate() {
        // given
        MedicalAppointmentDate expectedMedicalAppointmentDate = EntityFixtures.someMedicalAppointmentDate1();
        MedicalAppointmentDate notExpectedMedicalAppointmentDate = EntityFixtures.someMedicalAppointmentDate2();

        Mockito.when(medicalAppointmentDateDAO.saveMedicalAppointmentDate(Mockito.any(MedicalAppointmentDate.class)))
                .thenReturn(expectedMedicalAppointmentDate);

        // when
        MedicalAppointmentDate result = medicalAppointmentDateService
                .saveMedicalAppointmentDate(expectedMedicalAppointmentDate);

        // then
        Assertions.assertEquals(expectedMedicalAppointmentDate, result);
        Assertions.assertNotEquals(notExpectedMedicalAppointmentDate, result);

        Mockito.verify(medicalAppointmentDateDAO, Mockito.times(1))
                .saveMedicalAppointmentDate(Mockito.any(MedicalAppointmentDate.class));
        Mockito.verify(medicalAppointmentDateDAO, Mockito.never())
                .saveMedicalAppointmentDate(notExpectedMedicalAppointmentDate);

    }

}