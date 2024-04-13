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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.szczecin.business.dao.PatientDAO;
import pl.szczecin.domain.MedicalAppointmentRequest;
import pl.szczecin.domain.Patient;
import pl.szczecin.domain.PatientHistory;
import pl.szczecin.domain.exception.NotFoundException;
import pl.szczecin.util.EntityFixtures;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientDAO patientDAO;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private PatientService patientService;


    @BeforeEach
    public void setUp() {
        System.out.println("checking for nulls");
        Assertions.assertNotNull(patientDAO);
        Assertions.assertNotNull(passwordEncoder);
        Assertions.assertNotNull(securityContext);
    }


    @Test
    @DisplayName("That method should correctly save and return correct patient")
    void shouldCorrectlySavePatientAndReturnCorrectPatient() {
        //given
        MedicalAppointmentRequest medicalAppointmentRequest = EntityFixtures.someMedicalAppointmentRequest();
        Patient expectedPatient = EntityFixtures.somePatient1();
        Mockito.when(patientDAO.savePatient(Mockito.any(Patient.class))).thenReturn(expectedPatient);

        //when
        Patient resultPatient = patientService.savePatient(medicalAppointmentRequest);

        //then
        Assertions.assertEquals(expectedPatient, resultPatient);

        Mockito.verify(patientDAO, Mockito.times(1))
                .savePatient(Mockito.any(Patient.class));
        Mockito.verify(patientDAO, Mockito.never())
                .savePatient(expectedPatient.withName("other"));
        Mockito.verify(passwordEncoder, Mockito.times(1))
                .encode(Mockito.any());
    }

    @Test
    @DisplayName("That method should correctly return available patients")
    void shouldCorrectlyReturnAvailablePatients() {
        //given
        List<Patient> expectedPatients = List.of(
                EntityFixtures.somePatient1(),
                EntityFixtures.somePatient2(),
                EntityFixtures.somePatient3()
        );
        Mockito.when(patientDAO.findAvailablePatients(2,3)).thenReturn(expectedPatients);

        //when
        List<Patient> resultPatient = patientService.findAvailablePatients(2,3);

        //then
        Assertions.assertEquals(expectedPatients, resultPatient);

        Mockito.verify(patientDAO, Mockito.times(1)).findAvailablePatients(2,3);

        Mockito.verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("That method should return correct patient with given patient email")
    void shouldReturnCorrectPatientWithGivenEmail() {
        //given
        String patientEmail = "janusz.pacjent@clinic.pl";
        Patient expectedPatient = EntityFixtures.somePatient1();
        Mockito.when(patientDAO.findPatientByEmail(patientEmail)).thenReturn(Optional.of(expectedPatient));

        //when
        Patient resultPatient = patientService.findPatientByEmail(patientEmail);

        //then
        Assertions.assertEquals(expectedPatient, resultPatient);

        Mockito.verify(patientDAO, Mockito.times(1))
                .findPatientByEmail(Mockito.anyString());
        Mockito.verify(patientDAO, Mockito.never())
                .findPatientByEmail("other.email@clinic.pl");

        Mockito.verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("That method should return empty patient and throw NotFoundException with given patient email")
    void shouldReturnEmptyPatientAndThrowNotFoundExceptionWithGivenEmail() {
        //given
        String patientEmail = "janusz.pacjent@clinic.pl";
        Optional<Patient> expectedPatient = Optional.empty();
        Mockito.when(patientDAO.findPatientByEmail(patientEmail)).thenReturn(expectedPatient);

        // when, then
        NotFoundException exception =
                Assertions.assertThrows(NotFoundException.class, () -> patientService.findPatientByEmail(patientEmail));
        Assertions.assertEquals((
                        "Could not find patient by email: [%s]".formatted(patientEmail)),
                exception.getMessage());
    }

    @Test
    @DisplayName("That method should return correct patient history with given patient email")
    void shouldReturnCorrectPatientHistoryWithGivenEmail() {
        //given
        String patientEmail = "janusz.pacjent@clinic.pl";
        PatientHistory expectedPatientHistory = EntityFixtures.somePatientHistory();
        Mockito.when(patientDAO.findPatientHistoryByEmail(patientEmail)).thenReturn(expectedPatientHistory);

        //when
        PatientHistory resultPatientHistory = patientService.findPatientHistoryByEmail(patientEmail);

        //then
        Assertions.assertEquals(expectedPatientHistory, resultPatientHistory);

        Mockito.verify(patientDAO, Mockito.times(1))
                .findPatientHistoryByEmail(Mockito.anyString());
        Mockito.verify(patientDAO, Mockito.never())
                .findPatientHistoryByEmail("other.email@clinic.pl");

        Mockito.verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("That method should return correct patient schedule with given patient email")
    void shouldReturnCorrectPatientScheduleWithGivenEmail() {
        //given
        String patientEmail = "janusz.pacjent@clinic.pl";
        PatientHistory expectedPatientSchedule = EntityFixtures.somePatientHistory();
        Mockito.when(patientDAO.findPatientScheduleByEmail(patientEmail)).thenReturn(expectedPatientSchedule);

        //when
        PatientHistory result = patientService.findPatientScheduleByEmail(patientEmail);

        //then
        Assertions.assertEquals(expectedPatientSchedule, result);

        Mockito.verify(patientDAO, Mockito.times(1))
                .findPatientScheduleByEmail(Mockito.anyString());
        Mockito.verify(patientDAO, Mockito.never())
                .findPatientScheduleByEmail("other.email@clinic.pl");

        Mockito.verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("That method should return correct patient appointments to cancel with given email")
    void shouldReturnCorrectPatientAppointmentsToCancelWithGivenEmail() {
        //given
        String patientEmail = "janusz.pacjent@clinic.pl";
        PatientHistory expectedPatientAppointmentsToCancel = EntityFixtures.somePatientHistory();
        Mockito.when(patientDAO.findPatientAppointmentsToCancelByEmail(patientEmail)).thenReturn(expectedPatientAppointmentsToCancel);

        //when
        PatientHistory result = patientService.findPatientAppointmentsToCancelByEmail(patientEmail);

        //then
        Assertions.assertEquals(expectedPatientAppointmentsToCancel, result);

        Mockito.verify(patientDAO, Mockito.times(1))
                .findPatientAppointmentsToCancelByEmail(Mockito.anyString());
        Mockito.verify(patientDAO, Mockito.never())
                .findPatientAppointmentsToCancelByEmail("other.email@clinic.pl");

        Mockito.verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("That method should return correct patient email for logged-in patient")
    void shouldReturnCorrectPatientEmailForLoggedInPatient() {
        // given
        UserDetails userDetails = new User(
                "patient.patient@clinic.pl",
                "password",
                Collections.emptyList()); // pusta lista ról

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        // when
        String result = patientService.getLoggedInPatientEmail();

        // then
        Assertions.assertEquals("patient.patient@clinic.pl", result);
        Assertions.assertNotEquals("other.email@clinic.pl", result);

        Mockito.verify(securityContext, Mockito.times(1))
                .getAuthentication();

        Mockito.verifyNoInteractions(patientDAO);
        Mockito.verifyNoInteractions(passwordEncoder);
    }

    @Test
    @DisplayName("That method should throw NotFoundException when authentication is not present")
    void shouldThrowNotFoundExceptionWhenAuthenticationIsNotPresent() {
        // given
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(securityContext.getAuthentication()).thenReturn(null);

        // when, then
        NotFoundException exception =
                Assertions.assertThrows(NotFoundException.class, () -> patientService.getLoggedInPatientEmail());
        Assertions.assertEquals((
                        "Something went wrong because the email for logged-in patient could not be found."),
                exception.getMessage());

        Mockito.verify(securityContext, Mockito.times(1))
                .getAuthentication();

        Mockito.verifyNoInteractions(patientDAO);
        Mockito.verifyNoInteractions(passwordEncoder);
    }

}
