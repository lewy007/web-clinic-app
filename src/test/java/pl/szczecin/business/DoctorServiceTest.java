package pl.szczecin.business;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
import pl.szczecin.business.dao.DoctorDAO;
import pl.szczecin.domain.Doctor;
import pl.szczecin.domain.exception.NotFoundException;
import pl.szczecin.util.EntityFixtures;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {

    @Mock
    private DoctorDAO doctorDAO;
    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private DoctorService doctorService;


    @BeforeEach
    public void setUp() {
        Assertions.assertNotNull(doctorDAO);
        Assertions.assertNotNull(securityContext);
    }

    @Test
    @DisplayName("That method should return correct list of available doctors")
    void shouldCorrectlyReturnAvailableDoctors() {
        //given
        List<Doctor> expectedListDoctor = List.of(
                EntityFixtures.someDoctor1(),
                EntityFixtures.someDoctor2(),
                EntityFixtures.someDoctor3()
        );
        Mockito.when(doctorDAO.findAvailableDoctors()).thenReturn(expectedListDoctor);

        //when
        List<Doctor> resultAvailableDoctors = doctorService.findAvailableDoctors();

        //then
        Assertions.assertEquals(3, resultAvailableDoctors.size());

        Mockito.verify(doctorDAO, Mockito.times(1)).findAvailableDoctors();
    }

    @Test
    @DisplayName("That method should return correct doctor with given email")
    void shouldReturnCorrectDoctorWithGivenEmail() {
        //given
        String doctorEmail = "edyta.kowalska@clinic.pl";
        Doctor expectedDoctor = EntityFixtures.someDoctor1();

        Mockito.when(doctorDAO.findDoctorByEmail(doctorEmail)).thenReturn(Optional.of(expectedDoctor));

        //when
        Doctor resultDoctor = doctorService.findDoctorByEmail(doctorEmail);

        //then
        Assertions.assertEquals(expectedDoctor, resultDoctor);

        Mockito.verify(doctorDAO, Mockito.times(1))
                .findDoctorByEmail(Mockito.anyString());
        Mockito.verify(doctorDAO, Mockito.never())
                .findDoctorByEmail("other.email@clinic.pl");
    }

    @Test
    @DisplayName("That method should return empty doctor and throw NotFoundException with given email")
    void shouldReturnEmptyDoctorAndThrowNotFoundExceptionWithGivenEmail() {
        //given
        String doctorEmail = "edyta.kowalska@clinic.pl";
        Optional<Doctor> expectedDoctor = Optional.empty();

        Mockito.when(doctorDAO.findDoctorByEmail(doctorEmail)).thenReturn(expectedDoctor);

        // when, then
        NotFoundException exception =
                Assertions.assertThrows(NotFoundException.class, () -> doctorService.findDoctorByEmail(doctorEmail));
        Assertions.assertEquals((
                        "Could not find doctor by email: [%s]".formatted(doctorEmail)),
                exception.getMessage());

        Mockito.verify(doctorDAO, Mockito.times(1))
                .findDoctorByEmail(Mockito.anyString());
        Mockito.verify(doctorDAO, Mockito.never())
                .findDoctorByEmail("other.email@clinic.pl");
    }

    @ParameterizedTest
    @MethodSource("shouldReturnCorrectDoctorWithGivenSurname")
    @DisplayName("That method should return correct doctor by surname")
    void shouldReturnCorrectDoctorWithGivenSurname(Boolean correctSurname, String surname) {
        //given
        Doctor expectedDoctor = EntityFixtures.someDoctor1();

        //when, then
        if (correctSurname) {
            Mockito.when(doctorDAO.findDoctorBySurname(surname)).thenReturn(Optional.of(expectedDoctor));
            Doctor resultDoctor = doctorService.findDoctorBySurname(surname);

            Assertions.assertEquals(expectedDoctor, resultDoctor);

        } else {
            // Wywołanie testowanej metody i sprawdzenie wyjątku NotFoundException
            Mockito.when(doctorDAO.findDoctorBySurname(surname))
                    .thenThrow(new NotFoundException("Could not find doctor by surname: [%s]".formatted(surname)));

            NotFoundException expectedException = Assertions.assertThrows(NotFoundException.class,
                    () -> doctorDAO.findDoctorBySurname(surname));

            // Sprawdzenie treści wiadomości wyjątku
            Assertions.assertEquals(
                    expectedException.getMessage(),
                    "Could not find doctor by surname: [%s]".formatted(surname));
        }

    }

    public static Stream<Arguments> shouldReturnCorrectDoctorWithGivenSurname() {
        return Stream.of(
                Arguments.of(true, "Kowalski"),
                Arguments.of(true, "Nowak"),
                Arguments.of(true, "Jankowski"),
                Arguments.of(true, "Janiak"),
                Arguments.of(true, "Wasilewski"),
                Arguments.of(false, "Unknown"),
                Arguments.of(false, "Nieznany")
        );
    }


    // Poniższa metoda dodana, bo parametryzowana nie pokrywa testów
    @Test
    @DisplayName("That method should return empty doctor and throw NotFoundException with given surname")
    void shouldReturnEmptyDoctorAndThrowNotFoundExceptionWithGivenSurname() {
        //given
        String doctorSurname = "Kowalska";
        Optional<Doctor> expectedDoctor = Optional.empty();

        Mockito.when(doctorDAO.findDoctorBySurname(doctorSurname)).thenReturn(expectedDoctor);

        // when, then
        NotFoundException exception =
                Assertions.assertThrows(NotFoundException.class, () -> doctorService.findDoctorBySurname(doctorSurname));
        Assertions.assertEquals((
                        "Could not find doctor by surname: [%s]".formatted(doctorSurname)),
                exception.getMessage());

        Mockito.verify(doctorDAO, Mockito.times(1))
                .findDoctorBySurname(Mockito.anyString());
        Mockito.verify(doctorDAO, Mockito.never())
                .findDoctorBySurname("Janikowska");
    }

    @Test
    @DisplayName("That method should return correct patient email for logged-in patient")
    void shouldReturnCorrectPatientEmailForLoggedInPatient() {
        // given
        UserDetails userDetails = new User(
                "doctor.doctor@clinic.pl",
                "password",
                Collections.emptyList()); // pusta lista ról

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                null);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);

        // when
        String result = doctorService.getLoggedInDoctorEmail();

        // then
        Assertions.assertEquals("doctor.doctor@clinic.pl", result);
        Assertions.assertNotEquals("other.email@clinic.pl", result);

        Mockito.verify(securityContext, Mockito.times(1))
                .getAuthentication();

        Mockito.verifyNoInteractions(doctorDAO);
    }

    // Test dziala jak w metodzie getLoggedInDoctorEmail() jest zwracany wyjatek,
    // ale wtedy tzreba pozmieniac test integracyjny DoctorControllerIT na 404
//    @Test
//    @DisplayName("That method should throw NotFoundException when authentication is not present")
//    void shouldThrowNotFoundExceptionWhenAuthenticationIsNotPresent() {
//        // given
//        SecurityContextHolder.setContext(securityContext);
//
//        Mockito.when(securityContext.getAuthentication()).thenReturn(null);
//
//        // when, then
//        NotFoundException exception =
//                Assertions.assertThrows(NotFoundException.class, () -> doctorService.getLoggedInDoctorEmail());
//        Assertions.assertEquals((
//                        "Something went wrong because the email for logged-in doctor could not be found."),
//                exception.getMessage());
//
//        Mockito.verify(securityContext, Mockito.times(1))
//                .getAuthentication();
//
//        Mockito.verifyNoInteractions(doctorDAO);
//    }

}